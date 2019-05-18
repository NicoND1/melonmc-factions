package de.melonmc.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created on 27.04.2019
 *
 * @author RufixHD
 */

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        event.setFormat(player.getDisplayName() + "§8: §7" + event.getMessage());
    }
}
