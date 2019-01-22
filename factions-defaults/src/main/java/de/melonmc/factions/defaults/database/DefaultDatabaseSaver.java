package de.melonmc.factions.defaults.database;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import de.melonmc.factions.chestshop.Chestshop;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.chunk.ClaimableChunk.Flag;
import de.melonmc.factions.database.DatabaseSaver;
import de.melonmc.factions.database.DefaultConfigurations;
import de.melonmc.factions.database.NpcInformation;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.stats.Stats;
import de.melonmc.factions.util.ConfigurableLocation;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Nico_ND1
 */
public class DefaultDatabaseSaver implements DatabaseSaver {

    private static final String HOMES_COLLECTION = "factory-homes";
    private static final String PLAYERS_COLLECTION = "factory-players";
    private static final String FACTORY_COLLECTION = "factory-factories";
    private static final String DEFAULT_CONFIGURATION_COLLECTION = "factory-configuration";
    private static final String CHESTSHOP_COLLECTION = "factory-chestshop";

    private final MongoDatabase mongoDatabase;
    private final ExecutorService executorService = Executors.newFixedThreadPool(3, new ThreadFactory() {
        final AtomicLong atomicLong = new AtomicLong();

        @Override
        public Thread newThread(Runnable runnable) {
            final Thread thread = new Thread(runnable);
            thread.setName("Database Thread #" + this.atomicLong.getAndIncrement());
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());

            return thread;
        }
    });
    private final Cache<UUID, String> nameCache = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.HOURS)
        .build();
    private final Map<UUID, List<Home>> homes = new HashMap<>();
    private final Map<UUID, List<Chestshop>> chestshops = new HashMap<>();
    private final List<FactionsPlayer> factionsPlayers = new ArrayList<>();
    private final List<Faction> factions = new ArrayList<>();
    private DefaultConfigurations defaultConfigurations;

    public DefaultDatabaseSaver(MongoConfig mongoConfig) {
        final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs(new UuidCodec(UuidRepresentation.STANDARD)),
            MongoClient.getDefaultCodecRegistry()
        );
        final MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
            .codecRegistry(codecRegistry)
            .build();

        final MongoClient mongoClient = new MongoClient(new ServerAddress(
            mongoConfig.getHost(),
            mongoConfig.getPort()
        ), Collections.singletonList(MongoCredential.createCredential(
            mongoConfig.getUsername(),
            mongoConfig.getDatabase(),
            mongoConfig.getPassword().toCharArray()
        )), mongoClientOptions);
        this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
    }

    private String getName(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null)
            return Bukkit.getPlayer(uuid).getName();
        else
            return this.nameCache.getIfPresent(uuid);
    }

    private void runAction(Runnable runnable) {
        if (Thread.currentThread().getName().startsWith("Database Thread #"))
            runnable.run();
        else
            this.executorService.submit(runnable);
    }

    private Bson createPlayerFilter(String nameField, String uuidField, FactionsPlayer factionsPlayer) {
        if (factionsPlayer.getUuid() == null)
            return Filters.eq(nameField, factionsPlayer.getName());
        else
            return Filters.eq(uuidField, factionsPlayer.getUuid());
    }

    private Document createStatsDocument(Stats stats) {
        return new Document("kills", stats.getKills())
            .append("deaths", stats.getDeaths());
    }

    private Stats fromStatsDocument(Document document) {
        return new Stats(document.getLong("kills"), document.getLong("deaths"));
    }

    @Override
    public void notifyPlayerQuit(UUID uuid) {
        final Optional<FactionsPlayer> optionalFactionsPlayer = this.factionsPlayers.stream()
            .filter(factionsPlayer -> factionsPlayer.getUuid().equals(uuid))
            .findAny();

        optionalFactionsPlayer.ifPresent(factionsPlayer -> {
            synchronized (this.factionsPlayers) {
                this.factionsPlayers.remove(factionsPlayer);
            }
        });
        this.nameCache.invalidate(uuid);
        this.homes.remove(uuid);
        this.chestshops.remove(uuid);
    }

    @Override
    public void saveHome(Home home, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(HOMES_COLLECTION);
            final Document document = new Document("name", home.getName())
                .append("location", home.getLocation().createDocument())
                .append("uuid", home.getFactionsPlayer().getUuid());

            collection.updateOne(Filters.and(
                Filters.eq("name", home.getName()),
                Filters.eq("uuid", home.getFactionsPlayer().getUuid())
            ), document, new UpdateOptions().upsert(true));
        });
    }

    @Override
    public void deleteHome(Home home, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(HOMES_COLLECTION);
            collection.deleteOne(Filters.and(
                Filters.eq("name", home.getName()),
                Filters.eq("uuid", home.getFactionsPlayer().getUuid())
            ));
        });
    }

    @Override
    public void findHome(FactionsPlayer factionsPlayer, String name, Consumer<Optional<Home>> consumer) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(HOMES_COLLECTION);
            final FindIterable<Document> findIterable = collection.find(Filters.and(
                Filters.eq("uuid", factionsPlayer.getUuid()),
                Filters.eq("name", name)
            ));
            final Document document = findIterable.first();
            if (document == null) {
                consumer.accept(Optional.empty());
                return;
            }

            final Home home = new Home(
                new FactionsPlayer(document.get("uuid", UUID.class), this.getName(document.get("uuid", UUID.class)), null, null, 0),
                name,
                new ConfigurableLocation(document.get("location", Document.class))
            );
            consumer.accept(Optional.of(home));
        });
    }

    @Override
    public void findHomes(FactionsPlayer factionsPlayer, Consumer<List<Home>> consumer) {
        if (this.homes.containsKey(factionsPlayer.getUuid())) {
            consumer.accept(this.homes.get(factionsPlayer.getUuid()));
            return;
        }
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(HOMES_COLLECTION);
            final FindIterable<Document> findIterable = collection.find(Filters.eq("uuid", factionsPlayer.getUuid()));
            final List<Home> homes = new ArrayList<>();

            findIterable.forEach((Block<Document>) document -> homes.add(new Home(
                new FactionsPlayer(document.get("uuid", UUID.class), this.getName(document.get("uuid", UUID.class)), null, null, 0),
                document.getString("name"),
                new ConfigurableLocation(document.get("location", Document.class))
            )));

            consumer.accept(homes);
            this.homes.put(factionsPlayer.getUuid(), homes);
        });
    }

    @Override
    public void savePlayer(FactionsPlayer factionsPlayer, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(PLAYERS_COLLECTION);
            final Document document = new Document("uuid", factionsPlayer.getUuid())
                .append("name", factionsPlayer.getName())
                .append("coins", factionsPlayer.getCoins())
                .append("stats", this.createStatsDocument(factionsPlayer.getStats()));

            collection.updateOne(
                this.createPlayerFilter("name", "uuid", factionsPlayer),
                document,
                new UpdateOptions().upsert(true)
            );
        });
    }

    @Override
    public void deletePlayer(FactionsPlayer factionsPlayer, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(PLAYERS_COLLECTION);
            collection.deleteOne(this.createPlayerFilter("name", "uuid", factionsPlayer));
        });
    }

    @Override
    public void findPlayer(FactionsPlayer currentFactionsPlayer, Consumer<Optional<FactionsPlayer>> consumer) {
        final Optional<FactionsPlayer> optionalFactionsPlayer = this.factionsPlayers.stream()
            .filter(factionsPlayer -> factionsPlayer.getUuid().equals(currentFactionsPlayer.getUuid()) || factionsPlayer.getName().equals(currentFactionsPlayer.getName()))
            .findAny();
        if (optionalFactionsPlayer.isPresent()) {
            consumer.accept(optionalFactionsPlayer);
            return;
        }
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(PLAYERS_COLLECTION);
            final FindIterable<Document> findIterable = collection.find(this.createPlayerFilter("name", "uuid", currentFactionsPlayer));
            final Document document = findIterable.first();
            if (document == null) {
                consumer.accept(Optional.empty());
                return;
            }

            final Player player = currentFactionsPlayer.getName() == null ?
                Bukkit.getPlayer(currentFactionsPlayer.getUuid()) :
                Bukkit.getPlayer(currentFactionsPlayer.getName());
            final FactionsPlayer factionsPlayer = new FactionsPlayer(
                document.get("uuid", UUID.class),
                document.getString("name"),
                player,
                this.fromStatsDocument(document.get("stats", Document.class)),
                document.getLong("coins")
            );
            consumer.accept(Optional.of(factionsPlayer));

            if (player != null) this.factionsPlayers.add(factionsPlayer);
        });
    }

    @Override
    public void saveFaction(Faction faction, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(FACTORY_COLLECTION);
            final Document document = new Document("name", faction.getName())
                .append("tag", faction.getTag())
                .append("stats", this.createStatsDocument(faction.getStats()))
                .append("elo-points", faction.getEloPoints())
                .append("invited-players", faction.getInvitedPlayers().stream().map(factionsPlayer -> new Document("uuid", factionsPlayer.getUuid())
                    .append("name", factionsPlayer.getName()))
                    .collect(Collectors.toList()))
                .append("members", faction.getMembers().entrySet().stream().map(entry -> new Document("player", new Document("uuid", entry.getKey().getUuid())
                    .append("name", entry.getKey().getName()))
                    .append("rank", entry.getValue().ordinal()))
                    .collect(Collectors.toList()))
                .append("location", faction.getLocation().createDocument())
                .append("chunks", faction.getChunks().stream().map(claimableChunk -> new Document("x", claimableChunk.getX())
                    .append("z", claimableChunk.getZ())
                    .append("worldName", claimableChunk.getWorldName())
                    .append("flags", claimableChunk.getFlags().entrySet().stream().map(entry -> new Document("flag", entry.getKey().ordinal())
                        .append("value", entry.getValue()))
                        .collect(Collectors.toList())
                    )).collect(Collectors.toList())
                );

            collection.updateOne(Filters.eq("name", faction.getName()), document, new UpdateOptions().upsert(true));

            runnable.run();
        });
    }

    @Override
    public void deleteFaction(Faction faction, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(FACTORY_COLLECTION);
            collection.deleteOne(Filters.or(
                Filters.eq("name", faction.getName()),
                Filters.eq("tag", faction.getTag())
            ));

            runnable.run();
        });
    }

    @Override
    public void loadFaction(Faction originFaction, Consumer<Optional<Faction>> consumer) {
        final Optional<Faction> optionalFaction = this.factions.stream()
            .filter(faction -> faction.getName().equalsIgnoreCase(originFaction.getName()))
            .findAny();
        if (optionalFaction.isPresent()) {
            consumer.accept(optionalFaction);
            return;
        }

        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(FACTORY_COLLECTION);
            final FindIterable<Document> findIterable = collection.find(Filters.or(
                Filters.eq("name", originFaction.getName()),
                Filters.eq("tag", originFaction.getTag())
            ));
            final Document document = findIterable.first();
            if (document == null) {
                consumer.accept(Optional.empty());
                return;
            }

            final Map<FactionsPlayer, Rank> members = new HashMap<FactionsPlayer, Rank>() {{
                document.get("members", List.class).forEach((Consumer<Document>) doc -> this.put(new FactionsPlayer(
                    doc.get("player", Document.class).get("uuid", UUID.class),
                    doc.get("player", Document.class).getString("name"),
                    null
                ), Rank.values()[Math.min(doc.getInteger("rank"), Rank.values().length - 1)]));
            }};
            final List<FactionsPlayer> invitedPlayers = new ArrayList<FactionsPlayer>() {{
                document.get("invited-players", List.class).forEach((Consumer<Document>) doc -> this.add(new FactionsPlayer(
                    doc.get("uuid", UUID.class),
                    doc.getString("name"),
                    null
                )));
            }};
            final String name = document.getString("name");
            final String tag = document.getString("tag");
            final Stats stats = this.fromStatsDocument(document.get("stats", Document.class));
            final List<ClaimableChunk> chunks = new ArrayList<ClaimableChunk>() {{
                document.get("chunks", List.class).forEach((Consumer<Document>) doc -> this.add(new ClaimableChunk(
                    doc.getInteger("x"),
                    doc.getInteger("z"),
                    doc.getString("worldName"),
                    new HashMap<Flag, Boolean>() {{
                        doc.get("flags", List.class).forEach((Consumer<Document>) doc1 -> this.put(
                            Flag.values()[Math.min(doc1.getInteger("flag"), Flag.values().length - 1)],
                            doc1.getBoolean("value")
                        ));
                    }}, null
                )));
            }};
            final ConfigurableLocation location = new ConfigurableLocation(document.get("location", Document.class));
            final long eloPoints = document.getLong("elo-points");
            final Faction faction = new Faction(members, invitedPlayers, name, tag, stats, chunks, location, eloPoints);

            consumer.accept(Optional.of(faction));
            this.factions.add(faction);
        });
    }

    @Override
    public void saveDefaultConfigurations(DefaultConfigurations defaultConfigurations, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(DEFAULT_CONFIGURATION_COLLECTION);
            collection.updateOne(Filters.not(Filters.eq("Im not needed", "LOL")),
                new Document("spawn-location", defaultConfigurations.getSpawnLocation().createDocument())
                    .append("npcs", defaultConfigurations.getNpcInformations().stream()
                        .map(npcInformation -> new Document("location", npcInformation.getLocation().createDocument())
                            .append("teleport-location", npcInformation.getTeleportLocation().createDocument())
                            .append("name-header", npcInformation.getNameHeader())
                            .append("name-footer", npcInformation.getNameFooter()))
                        .collect(Collectors.toList())
                    ));
        });
    }

    @Override
    public void loadDefaultConfigurations(Consumer<DefaultConfigurations> consumer) {
        if (this.defaultConfigurations != null) {
            consumer.accept(this.defaultConfigurations);
            return;
        }

        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(DEFAULT_CONFIGURATION_COLLECTION);
            final FindIterable<Document> findIterable = collection.find(Filters.not(Filters.eq("Im not needed", "LOL")));
            final Document document = findIterable.first();
            if (document == null) {
                consumer.accept(new DefaultConfigurations(
                    new ConfigurableLocation("world", 0, 0, 0, 0, 0),
                    new ArrayList<>()
                ));
                return;
            }

            final DefaultConfigurations defaultConfigurations = new DefaultConfigurations(
                new ConfigurableLocation(document.get("spawn-location", Document.class)),
                new ArrayList<NpcInformation>() {{
                    document.get("npcs", List.class).forEach((Consumer<Document>) doc -> this.add(new NpcInformation(
                        doc.getString("name-header"),
                        doc.getString("name-footer"),
                        new ConfigurableLocation(doc.get("location", Document.class)),
                        new ConfigurableLocation(doc.get("teleport-location", Document.class))
                    )));
                }}
            );
            consumer.accept(defaultConfigurations);
            this.defaultConfigurations = defaultConfigurations;
        });
    }

    @Override
    public void saveChestshop(Chestshop chestshop, Runnable runnable) {
        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(CHESTSHOP_COLLECTION);
            collection.updateOne(Filters.eq("id", chestshop.getId()), new Document("id", chestshop.getId())
                .append("owner", new Document("uuid", chestshop.getOwner().getUuid()).append("name", chestshop.getOwner().getName()))
                .append("amont", chestshop.getAmount())
                .append("costs", chestshop.getCosts())
                .append("displayname", chestshop.getDisplayName())
                .append("chest-location", chestshop.getChestLocation().createDocument())
                .append("sign-location", chestshop.getSignLocation().createDocument())
                .append("itemstack", new Document("type", chestshop.getItemStack().getType().name())
                    .append("data", chestshop.getItemStack().getData().getData())
                ), new UpdateOptions().upsert(true));
        });
    }

    @Override
    public void loadChestshops(FactionsPlayer factionsPlayer, Consumer<List<Chestshop>> consumer) {
        if (this.chestshops.containsKey(factionsPlayer.getUuid())) {
            consumer.accept(this.chestshops.get(factionsPlayer.getUuid()));
            return;
        }

        this.runAction(() -> {
            final MongoCollection<Document> collection = this.mongoDatabase.getCollection(CHESTSHOP_COLLECTION);
            final FindIterable<Document> findIterable = collection.find(this.createPlayerFilter("owner.name", "owner.uuid", factionsPlayer));
            final List<Chestshop> chestshops = new ArrayList<>();

            findIterable.forEach((Block<Document>) document -> {
                chestshops.add(new Chestshop(
                    document.getString("id"),
                    factionsPlayer,
                    new ItemStack(
                        Material.valueOf(document.get("itemstack", Document.class).getString("type")),
                        1,
                        Short.valueOf(document.get("itemstack", Document.class).getInteger("data").toString())
                    ), document.getString("displayname"),
                    document.getInteger("amount"),
                    document.getInteger("costs"),
                    new ConfigurableLocation(document.get("sign-location", Document.class)),
                    new ConfigurableLocation(document.get("chest-location", Document.class))
                ));
            });

            consumer.accept(chestshops);
            this.chestshops.put(factionsPlayer.getUuid(), chestshops);
        });
    }
}
