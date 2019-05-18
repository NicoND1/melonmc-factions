package de.melonmc.bukkit.listener;

import de.melonmc.bukkit.Bootstrapable;
import de.melonmc.bukkit.manager.scoreboard.ScorebaordManager;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.job.Job;
import de.melonmc.factions.job.JobPlayer;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;

/**
 * Created on 31.03.2019
 *
 * @author RufixHD
 */

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage(null);

        Bukkit.getScheduler().runTaskLater(Factions.getPlugin(), new Runnable() {
            @Override
            public void run() {
                ScorebaordManager.setBoard(player);

                Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, player.getName(), null), optionalFactionsPlayer -> {
                    if (optionalFactionsPlayer.get().getCoins() == 0) {
                        Factions.getInstance().getDatabaseSaver().loadDefaultConfigurations(defaultConfigurations -> player.teleport(defaultConfigurations.getSpawnLocation().toLocation()));

                        Bootstrapable.getInstance().getNcps().forEach((npcInformation, npc) -> {
                            try {
                                npc.appear(player);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });

                Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
                    if (!optionalJobPlayer.isPresent()) {
                        player.sendMessage("§cEs kam ein Fehler auf.");
                        return;
                    }
                    final JobPlayer jobPlayer = optionalJobPlayer.get();

                    jobPlayer.getJobs().forEach((job) -> {
                        loadAction(jobPlayer, job);
                    });
                });

                Bukkit.getScheduler().runTaskLater(Factions.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage(Messages.PLAYER_JOIN.getMessage(player.getDisplayName()));
                    }
                }, 10);
            }
        }, 10);
    }

    //Hinzugefügt, da Spieler beim neubetreten erst ein Level aufsteigen müssen, damit sie Münzen bekommen.
    public void loadAction(JobPlayer jobPlayer, Job job) {
        job.incrementActions();
        job.setActions(job.getActions());
        job.setLevel(job.getLevel());
        if (job.getLevel() <= 10) {
            job.setCoinDiff(job.getCoinDiff() + (int) (JobPlayer.MONEY_PER_ACTION * (JobPlayer.MONEY_MULTIPLIER * job.getLevel() + 1)));
        } else {
            job.setLevel(10);
        }
    }
}
