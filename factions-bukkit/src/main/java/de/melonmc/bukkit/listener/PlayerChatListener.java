package de.melonmc.bukkit.listener;

import de.melonmc.factions.Factions;
import de.melonmc.factions.player.FactionsPlayer;
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

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, player.getName(), null), optionalFactionsPlayer -> {
            final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
            Factions.getInstance().getDatabaseSaver().findFaction(factionsPlayer, optionalFaction -> {
                event.setFormat("§a" + optionalFaction.get().getTag() + " §8» " + event.getPlayer().getDisplayName() + "§8: §7" + event.getMessage());
            });
        });
    }
}
