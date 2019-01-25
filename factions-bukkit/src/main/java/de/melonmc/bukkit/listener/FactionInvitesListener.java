package de.melonmc.bukkit.listener;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
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
            optionalFactionsPlayer.get().setName(player.getName());

            Factions.getInstance().getDatabaseSaver().savePlayer(factionsPlayer, () -> {
            });
        });

        Factions.getInstance().getDatabaseSaver().findFactionInvites(new FactionsPlayer(player), factionNames -> {
            final ComponentBuilder componentBuilder = new ComponentBuilder(Messages.FACTION_PLAYER_INVITES_NOTIFY.getMessage(
                factionNames.size(), factionNames.size() == 1 ? "" : "en"
            )).event(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("TEXT")}));

            player.spigot().sendMessage(componentBuilder.create());
        });
    }

}
