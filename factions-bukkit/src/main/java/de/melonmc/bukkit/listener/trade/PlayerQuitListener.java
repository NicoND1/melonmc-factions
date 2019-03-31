package de.melonmc.bukkit.listener.trade;

import de.melonmc.factions.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created on 31.03.2019
 *
 * @author RufixHD
 */

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();

        event.setQuitMessage(Messages.PLAYER_QUIT.getMessage(player.getDisplayName()));
    }
}
