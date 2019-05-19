package de.melonmc.bukkit;
import de.bergwerklabs.util.NPC;
import de.melonmc.bukkit.command.chunk.ChunkClaimCommand;
import de.melonmc.bukkit.command.chunk.ChunkListCommand;
import de.melonmc.bukkit.command.chunk.ChunkSettingsCommand;
import de.melonmc.bukkit.command.chunk.ChunkUnclaimCommand;
import de.melonmc.bukkit.command.faction.*;
import de.melonmc.bukkit.command.home.HomeCommand;
import de.melonmc.bukkit.command.home.HomeListCommand;
import de.melonmc.bukkit.command.home.HomeRemoveCommand;
import de.melonmc.bukkit.command.home.SetHomeCommand;
import de.melonmc.bukkit.command.job.JobsCommand;
import de.melonmc.bukkit.command.money.*;
import de.melonmc.bukkit.command.npc.NpcSetCommand;
import de.melonmc.bukkit.command.spawn.SpawnCommand;
import de.melonmc.bukkit.command.tpa.TpaAcceptCommand;
import de.melonmc.bukkit.command.tpa.TpaCommand;
import de.melonmc.bukkit.command.trade.TradeAcceptCommand;
import de.melonmc.bukkit.command.trade.TradeCommand;
import de.melonmc.bukkit.listener.*;
import de.melonmc.bukkit.listener.chestshop.ChestshopBuyListener;
import de.melonmc.bukkit.listener.chestshop.ChestshopCreateListener;
import de.melonmc.bukkit.listener.chestshop.ChestshopDestroyListener;
import de.melonmc.bukkit.listener.chestshop.ChestshopInteractListener;
import de.melonmc.bukkit.listener.job.*;
import de.melonmc.bukkit.listener.money.DepositMoneyListener;
import de.melonmc.bukkit.listener.trade.PlayerQuitListener;
import de.melonmc.bukkit.listener.trade.TradeListener;
import de.melonmc.bukkit.manager.scoreboard.ScorebaordManager;
import de.melonmc.factions.Factions;
import de.melonmc.factions.IBootstrapable;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.database.NpcInformation;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Nico_ND1
 */
@Data
public class Bootstrapable implements IBootstrapable {

    @Getter private static Bootstrapable instance;
    private final Map<NpcInformation, NPC> ncps = new HashMap<>();

    @Override
    public void onStart() {
        instance = this;

        ScorebaordManager.updateBoard();

        Factions.getInstance().getDatabaseSaver().loadAllFactions();

        Bukkit.getPluginManager().registerEvents(new FactionInvitesListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new NpcInteractListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new DepositMoneyListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), Factions.getPlugin());


        Factions.getPlugin().getServer().getPluginCommand("spawn").setExecutor(new SpawnCommand());

        Factions.getInstance().createCommandExecutor("npc", Collections.singletonList(new NpcSetCommand()));

        final List<ICommand> homeCommands = Arrays.asList(
            new HomeListCommand(),
            new HomeRemoveCommand(),
            new SetHomeCommand()
        );
        Factions.getPlugin().getServer().getPluginCommand("home").setExecutor(new HomeCommand(
            homeCommands,
            Factions.getInstance().createCommandExecutor("home", homeCommands)
        ));

        Factions.getInstance().createCommandExecutor("faction", Arrays.asList(
            new FactionCreateCommand(),
            new FactionInviteCommand(),
            new FactionListCommand(),
            new FactionAcceptCommand(),
            new FactionDenyCommand(),
            new FactionKickCommand(),
            new FactionLeaveCommand(),
            new FactionPromoteCommand(),
            new FactionStatsCommand(),
            new FactionSetBaseCommand(),
            new FactionBaseCommand(),
            new FactionTopListCommand()
        ));

        Factions.getInstance().createCommandExecutor("chunk", Arrays.asList(
            new ChunkClaimCommand(),
            new ChunkUnclaimCommand(),
            new ChunkSettingsCommand(),
            new ChunkListCommand()
        ));

        Factions.getInstance().createCommandExecutor("money", Arrays.asList(
            new MoneyPayCommand(),
            new MoneyAddCommand(),
            new MoneyRemoveCommand(),
            new MoneyWithdrawCommand(),
            new MoneyInfoCommand()
        ));
        /*
        final List<ICommand> tradeCommands = Arrays.asList(
            new TradeAcceptCommand()
        );
        Factions.getPlugin().getServer().getPluginCommand("trade").setExecutor(new TradeCommand(
            tradeCommands,
            Factions.getInstance().createCommandExecutor("trade", tradeCommands)
        ));
        */
        Bukkit.getPluginManager().registerEvents(new TradeListener(), Factions.getPlugin());

        Bukkit.getPluginManager().registerEvents(new ChestshopCreateListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new ChestshopDestroyListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new ChestshopInteractListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new ChestshopBuyListener(), Factions.getPlugin());

        Factions.getInstance().createCommandExecutor("jobs", Collections.singletonList(
            new JobsCommand()
        ));
        Factions.getInstance().getJobManager().registerJobListener(new MinerJobListener());
        Factions.getInstance().getJobManager().registerJobListener(new FarmerJobListener());
        Factions.getInstance().getJobManager().registerJobListener(new FishermanJobListener());
        Factions.getInstance().getJobManager().registerJobListener(new HunterJobListener());
        Factions.getInstance().getJobManager().registerJobListener(new SmithJobListener());
        Factions.getInstance().getJobManager().registerJobListener(new WizardJobListener());
        Factions.getInstance().getJobManager().registerJobListener(new WoodcutterJobListener());
        Bukkit.getPluginManager().registerEvents(new JobSettingsListener(), Factions.getPlugin());

        Bukkit.getServer().getPluginCommand("tpa").setExecutor(new TpaCommand(Collections.singletonList(new TpaAcceptCommand()),
            Factions.getInstance().createCommandExecutor("tpa", Collections.singletonList(new TpaAcceptCommand()))));

        Factions.getInstance().getDatabaseSaver().loadDefaultConfigurations(defaultConfigurations -> {
            defaultConfigurations.getNpcInformations().forEach(npcInformation -> {
                Bukkit.getScheduler().runTask(Factions.getPlugin(), () -> {
                    try {
                        final NPC npc = new NPC(
                            npcInformation.getNameHeader(),
                            npcInformation.getNameFooter(),
                            true,
                            true,
                            npcInformation.getLocation().toLocation()
                        );
                        npc.spawn();

                        if(npcInformation.getNameHeader().equals("§8» §aBauwelt§8-§aNormal"))
                        npc.updateSkin("eyJ0aW1lc3RhbXAiOjE1NTcwMDQ4ODg0ODIsInByb2ZpbGVJZCI6ImI4ZjYyYTMyMTE4ZDQ3MDZiYzExZTRmMzI3N2FkOTllIiwicHJvZmlsZU5hbWUiOiJGYXJtZXIiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY5NDI3MmNjZjZkYzljODA4MjFkZGFkMzI2MWE5MzAyNjYwMTU3OGUwMDRmNTM5YTRiODA3ZDk5MWJjNzdlMWYifX19", "uZt19CfmngQcjt2KhP+2mAKIyACl56SxB2luAOd6fqvmR9HskLyUGzdNYS3YIdFlcgKpRck39yM3gY/NQ1Qlx5XcOum7VJUEaHr3rBpOVrns/uvRSTz9wZhkUgcnVe8IAu4nho9hMOalM2loh10wLd6R5YEJCp7oWnFHLJ/8yMrmEiULLWsF39Hq5bKt53UgH+cIP77S6WK749AG1VwwGJepsCHXspJyOtLuti70p2VOWEgmAnU0B2RAZoprSMbnWJmAT8RLl3JUGaG1UNcZllJIGNL/Hs08wtjsYZ0DdNwd3x4c+TbRUqUoK5B2b+5VeXmBd9j1GVkf9vYeiL0vCJujdOuo4Y0iVgEOyRAf82dKZ1KAsYp7y3J0mx2acTaKTw0tlA2lepnHiPipNrLubur8m98suux2d6rpzm89ZeUM+HllOe/tDJUbFluixt0Vbw/pHDIGUSzjTNro9b+eOx4rpIrb0xFC7S4Qqx0xd/iLwnOmF1tG3teeJNqzq1KJCB3dhQRdzFeumkmgDWuSdK/YPQtC/dA/AScjujGRQvqiKRpUDzU4GcaEslP9TAw1Kd8VlDeSKs8iv2snL9kIKyMuUt5jJcvKr92VCQJCCL2cY52F501Cm8t/w9CK9pscQ263lNjpTFywKBhcQEzRpX8nNeJJpD4K8MbAc/mxnlk=");

                        if(npcInformation.getNameHeader().equals("§8» §aBauwelt§8-§aEpisch"))
                        npc.updateSkin("eyJ0aW1lc3RhbXAiOjE1NTcwMDU4NjkwMDEsInByb2ZpbGVJZCI6IjE1YTcwZGM4MDM4OTQ2MDRhY2E3NjQyZGQwOTdjN2E3IiwicHJvZmlsZU5hbWUiOiJkaXJ0Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81ZjRiNjUxY2NkYzcwODk5YmE2Y2IwMmFhMWE0ZmE5MjNlMWU2MDBjZTU5ZTUyODkwNDk1OWUwNDE1NmY0OGFhIn19fQ==", "elP3i/v6XfYoznEjrpaQjM9HIx1NsLWZi3iefm85m37SGFPpBj9GUBba2JJAR3wRZDZZhKXyBiLTlpNV5GQH34J7y12oqaGIkC26SQqm4QMdj55jYkZ+bo0RtID0nfjgUeO+YkIaYicOAsph6r+ZOLj6uJg+lQ6AfyhoRGorTtxCocTqGu89BqR/NTiaazKPTPfHNzhwZ8kDUsKDkQPyHASopxeBf1FhqW7fW3/4TXwfrAPJ7IYcfsA1Vqgsix+0bvT6BvPNfL5hUGwbLUTqABD8WHRngvZ/BQDDcNrEu8V8UUmA5fvFjSdq0BvdZydTEtHHrGCCEchUNDTAZZmmpt8OPO7+ilvzpomw1DNAVxgdScEElFfaJNh/voVrEr1CKv0KfGi1sW6epfgyB+pDit7ML7GxNxc6WyE9IpP3rMYWvWmWsteAm1bHIg7D0sOTI+Lw6XW7Nrvi/iuXU/84FIPIH6Ah2st9z3hK66CXAmxQPSm0RENPcaDje/iTvAi4cdGce9p+gWaYEI56ANExjG3Uc8f4dVGeFe5gYm8CUV5lrthWa86kKC3jBp3Y89Am1zHq0LR3bWK92rElbd7hzRL2amzGyiFkNHykjnKQFjfjy/yXSE/n1Fp9Qlg3PMNyNQdnGopq67mTL9ygMJjlwkphT6/K/wQWspK28NrHoNs=");

                        this.ncps.put(npcInformation, npc);
                    } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            });

            final World world = Bukkit.getWorld(defaultConfigurations.getSpawnLocation().getWorldName());
            if (world == null) {
                Factions.getPlugin().getLogger().log(Level.WARNING, "Spawn location is null, can't apply flags!");
                return;
            }

            Bukkit.getPluginManager().registerEvents(new SpawnWorldListener(world), Factions.getPlugin());
        });

        //Load Worlds
        Bukkit.createWorld(new WorldCreator("Bauwelt-Normal").generateStructures(false));
        Bukkit.getWorlds().add(Bukkit.getWorld("Bauwelt-Normal"));
        Bukkit.createWorld(new WorldCreator("Bauwelt-Episch").generateStructures(false));
        Bukkit.getWorlds().add(Bukkit.getWorld("Bauwelt-Episch"));
    }

    @Override
    public void onStop() {
        this.ncps.forEach((npcInformation, npc) -> {
            try {
                npc.despawn();
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
