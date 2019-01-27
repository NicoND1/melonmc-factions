package de.melonmc.bukkit.listener;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.job.JobPlayer;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.stats.Stats;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

/**
 * @author Nico_ND1
 */
public class FactionInvitesListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(player), optionalFactionsPlayer -> {
            if (!optionalFactionsPlayer.isPresent()) {
                Factions.getInstance().getDatabaseSaver().savePlayer(new FactionsPlayer(
                    player.getUniqueId(),
                    player.getName(),
                    player,
                    new Stats(0, 0),
                    0
                ), () -> {
                });
                return;
            }

            final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
            factionsPlayer.setName(player.getName());
            factionsPlayer.setPlayer(player);

            Factions.getInstance().getDatabaseSaver().savePlayer(factionsPlayer, () -> {
            });
        });
        Factions.getInstance().getDatabaseSaver().loadChestshops(new FactionsPlayer(player), chestshops -> {
        });
        Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
            if (!optionalJobPlayer.isPresent())
                Factions.getInstance().getDatabaseSaver().saveJobPlayer(new JobPlayer(player.getUniqueId(), new ArrayList<>()), () -> {
                });
        });

        Factions.getInstance().getDatabaseSaver().findFactionInvites(new FactionsPlayer(player), factionNames -> {
            if (factionNames.isEmpty()) return;

            final ComponentBuilder componentBuilder = new ComponentBuilder(Messages.FACTION_PLAYER_INVITES_NOTIFY.getMessage(
                factionNames.size(), factionNames.size() == 1 ? "" : "en"
            )).event(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("TEXT")}));

            player.spigot().sendMessage(componentBuilder.create());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        Factions.getInstance().getDatabaseSaver().notifyPlayerQuit(player.getUniqueId());
    }

}
