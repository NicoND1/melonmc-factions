package de.melonmc.bukkit;
import de.bergwerklabs.util.NPC;
import de.melonmc.bukkit.command.chunk.ChunkClaimCommand;
import de.melonmc.bukkit.command.chunk.ChunkSettingsCommand;
import de.melonmc.bukkit.command.chunk.ChunkUnclaimCommand;
import de.melonmc.bukkit.command.faction.*;
import de.melonmc.bukkit.command.home.HomeCommand;
import de.melonmc.bukkit.command.home.HomeListCommand;
import de.melonmc.bukkit.command.home.HomeRemoveCommand;
import de.melonmc.bukkit.command.home.SetHomeCommand;
import de.melonmc.bukkit.command.job.JobsCommand;
import de.melonmc.bukkit.command.money.MoneyAddCommand;
import de.melonmc.bukkit.command.money.MoneyPayCommand;
import de.melonmc.bukkit.command.money.MoneyRemoveCommand;
import de.melonmc.bukkit.command.npc.NpcSetCommand;
import de.melonmc.bukkit.command.spawn.SpawnCommand;
import de.melonmc.bukkit.command.tpa.TpaAcceptCommand;
import de.melonmc.bukkit.command.tpa.TpaCommand;
import de.melonmc.bukkit.command.trade.TradeAcceptCommand;
import de.melonmc.bukkit.command.trade.TradeCommand;
import de.melonmc.bukkit.listener.FactionInvitesListener;
import de.melonmc.bukkit.listener.NpcInteractListener;
import de.melonmc.bukkit.listener.SpawnWorldListener;
import de.melonmc.bukkit.listener.chestshop.ChestshopBuyListener;
import de.melonmc.bukkit.listener.chestshop.ChestshopCreateListener;
import de.melonmc.bukkit.listener.chestshop.ChestshopDestroyListener;
import de.melonmc.bukkit.listener.chestshop.ChestshopInteractListener;
import de.melonmc.bukkit.listener.job.*;
import de.melonmc.factions.Factions;
import de.melonmc.factions.IBootstrapable;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.database.NpcInformation;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

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

        Factions.getInstance().getDatabaseSaver().loadAllFactions();

        Bukkit.getPluginManager().registerEvents(new FactionInvitesListener(), Factions.getPlugin());
        Bukkit.getPluginManager().registerEvents(new NpcInteractListener(), Factions.getPlugin());

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
            new ChunkSettingsCommand()
        ));

        Factions.getInstance().createCommandExecutor("money", Arrays.asList(
            new MoneyPayCommand(),
            new MoneyAddCommand(),
            new MoneyRemoveCommand()
        ));

        final List<ICommand> tradeCommands = Arrays.asList(
            new TradeAcceptCommand()
        );
        Factions.getPlugin().getServer().getPluginCommand("trade").setExecutor(new TradeCommand(
            tradeCommands,
            Factions.getInstance().createCommandExecutor("trade", tradeCommands)
        ));

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
