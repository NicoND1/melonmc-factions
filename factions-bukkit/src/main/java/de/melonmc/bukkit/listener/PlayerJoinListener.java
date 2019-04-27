package de.melonmc.bukkit.listener;

import de.melonmc.bukkit.manager.scoreboard.ScorebaordManager;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created on 31.03.2019
 *
 * @author RufixHD
 */

public class PlayerJoinListener implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage(null);

        Bukkit.getScheduler().runTaskLater(Factions.getPlugin(), new Runnable() {
            @Override
            public void run() {
                ScorebaordManager.setBoard(player);
                Bukkit.getScheduler().runTaskLater(Factions.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage(Messages.PLAYER_JOIN.getMessage(player.getDisplayName()));
                    }
                }, 10);
            }
        }, 10);
    }
}
