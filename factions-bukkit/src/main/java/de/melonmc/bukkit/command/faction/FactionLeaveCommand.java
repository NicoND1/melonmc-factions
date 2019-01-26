package de.melonmc.bukkit.command.faction;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico_ND1
 */
public class FactionLeaveCommand implements ICommand<Player> {

    private final Cache<UUID, Long> timeout = CacheBuilder.newBuilder()
        .expireAfterWrite(30, TimeUnit.SECONDS)
        .build();

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"quit"};
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("not") && this.timeout.getIfPresent(player.getUniqueId()) != null) {
            this.timeout.invalidate(player.getUniqueId());
            player.sendMessage(Messages.FACTION_LEAVE_REQUEST_REJECT.getMessage());
            return Result.SUCCESSFUL;
        }

        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (!optionalFaction.isPresent()) {
                player.sendMessage(Messages.NOT_IN_A_FACTION.getMessage());
                return;
            }

            final Faction faction = optionalFaction.get();
            if (this.timeout.getIfPresent(player.getUniqueId()) != null) {
                this.timeout.invalidate(player.getUniqueId());
                faction.getMembers().keySet().stream()
                    .filter(factionsPlayer -> factionsPlayer.getUuid().equals(player.getUniqueId()))
                    .findFirst()
                    .ifPresent(factionsPlayer -> faction.getMembers().remove(factionsPlayer));
                if (faction.getMembers().values().stream().noneMatch(rank -> rank == Rank.ADMIN)) {
                    final Optional<Entry<FactionsPlayer, Rank>> optionalMod = faction.getMembers().entrySet().stream()
                        .filter(entry -> entry.getValue() == Rank.MODERATOR)
                        .findFirst();

                    if (optionalMod.isPresent()) {
                        final Entry<FactionsPlayer, Rank> entry = optionalMod.get();
                        final FactionsPlayer factionsPlayer = entry.getKey();
                        entry.setValue(Rank.ADMIN);

                        if (factionsPlayer.getName() == null) {
                            this.searchNameAndBroadcastUpdate(faction, factionsPlayer);
                            return;
                        } else {
                            faction.broadcast(Messages.FACTION_PLAYER_PROMOTE.getMessage(
                                factionsPlayer.getName(),
                                Rank.ADMIN.buildPrefix()
                            ));
                        }
                    } else {
                        faction.getMembers().entrySet().stream().findFirst().ifPresent(entry -> {
                            final FactionsPlayer factionsPlayer = entry.getKey();
                            entry.setValue(Rank.ADMIN);

                            if (factionsPlayer.getName() == null) {
                                this.searchNameAndBroadcastUpdate(faction, factionsPlayer);
                            } else {
                                faction.broadcast(Messages.FACTION_PLAYER_PROMOTE.getMessage(
                                    factionsPlayer.getName(),
                                    Rank.ADMIN.buildPrefix()
                                ));
                            }
                        });
                    }
                }

                player.sendMessage(Messages.FACTION_LEAVE.getMessage());
                if (faction.getMembers().isEmpty())
                    Factions.getInstance().getDatabaseSaver().deleteFaction(faction, () -> player.sendMessage(Messages.FACTION_DELETED.getMessage()));
                else
                    Factions.getInstance().getDatabaseSaver().saveFaction(faction, () -> faction.broadcast(Messages.FACTION_PLAYER_QUIT.getMessage(player.getName())));
                return;
            }
            this.timeout.put(player.getUniqueId(), System.currentTimeMillis());

            final ComponentBuilder componentBuilder = new ComponentBuilder(Messages.FACTION_LEAVE_REQUEST.getMessage())
                .append("\n ")
                .append("Ja")
                .color(ChatColor.GREEN)
                .event(new ClickEvent(Action.RUN_COMMAND, "/faction leave"))
                .append(" ")
                .append("Nein")
                .color(ChatColor.RED)
                .event(new ClickEvent(Action.RUN_COMMAND, "/faction leave not"));

            player.spigot().sendMessage(componentBuilder.create());
        });

        return Result.SUCCESSFUL;
    }

    private void searchNameAndBroadcastUpdate(Faction faction, FactionsPlayer factionsPlayer) {
        Factions.getInstance().getDatabaseSaver().findPlayer(factionsPlayer, optionalFactionsPlayer -> {
            if (!optionalFactionsPlayer.isPresent()) return;

            final FactionsPlayer realFactionsPlayer = optionalFactionsPlayer.get();
            faction.broadcast(Messages.FACTION_PLAYER_PROMOTE.getMessage(
                realFactionsPlayer.getName(),
                Rank.ADMIN.buildPrefix()
            ));
        });
    }
}
