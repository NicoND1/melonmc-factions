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
import de.melonmc.factions.database.DatabaseSaver;
import de.melonmc.factions.database.DefaultConfigurations;
import de.melonmc.factions.faction.Faction;
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
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author Nico_ND1
 */
public class DefaultDatabaseSaver implements DatabaseSaver {

    private static final String HOMES_COLLECTION = "factory-homes";
    private static final String PLAYERS_COLLECTION = "factory-players";

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
    private final List<FactionsPlayer> factionsPlayers = new ArrayList<>();

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

    }

    @Override
    public void deleteFaction(Faction faction, Runnable runnable) {

    }

    @Override
    public void loadFaction(Faction faction, Consumer<Optional<Faction>> consumer) {

    }

    @Override
    public void saveDefaultConfigurations(DefaultConfigurations defaultConfigurations, Runnable runnable) {

    }

    @Override
    public void loadDefaultConfigurations(Consumer<DefaultConfigurations> consumer) {

    }

    @Override
    public void saveChestshop(Chestshop chestshop, Runnable runnable) {

    }

    @Override
    public void loadChestshops(FactionsPlayer factionsPlayer, Consumer<Optional<List<Chestshop>>> consumer) {

    }
}
