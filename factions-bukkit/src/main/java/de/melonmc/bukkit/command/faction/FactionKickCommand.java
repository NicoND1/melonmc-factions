package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class FactionKickCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 1) return Result.WRONG_ARGUMENTS;

        final String name = args[0];
        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (!optionalFaction.isPresent()) {
                player.sendMessage(Messages.NOT_IN_A_FACTION.getMessage());
                return;
            }

            final Faction faction = optionalFaction.get();
            final Optional<Entry<FactionsPlayer, Rank>> optionalEntry = faction.getMembers().entrySet().stream()
                .filter(entry -> entry.getKey().getUuid().equals(player.getUniqueId()))
                .findFirst();
            if (!optionalEntry.isPresent()) {
                player.sendMessage("§cEs kam ein unbekannter Fehler auf.");
                return;
            }
            final Rank rank = optionalEntry.get().getValue();
            if (rank == Rank.PLAYER) {
                player.sendMessage(Messages.FACTION_KICK_NOT_ENOUGH_PERMISSIONS.getMessage());
                return;
            }

            final Optional<FactionsPlayer> optionalFactionsPlayer = faction.getMembers().keySet().stream()
                .filter(factionsPlayer -> factionsPlayer.getName().equalsIgnoreCase(name))
                .findFirst();
            if (!optionalFactionsPlayer.isPresent()) {
                player.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
                return;
            }
            final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
            final Rank factionsPlayerRank = faction.getMembers().get(factionsPlayer);
            if (rank == Rank.MODERATOR && factionsPlayerRank != Rank.PLAYER) {
                player.sendMessage(Messages.FACTION_KICK_NOT_ENOUGH_PERMISSIONS.getMessage());
                return;
            }

            faction.getMembers().remove(factionsPlayer);
            Factions.getInstance().getDatabaseSaver().saveFaction(faction, () -> {
                player.sendMessage(Messages.FACTION_PLAYER_KICK_SUCCESSFUL.getMessage(factionsPlayer.getName()));
                faction.getMembers().forEach((factionsPlayer1, rank1) -> {
                    if (factionsPlayer1.getPlayer() != null && factionsPlayer1.getPlayer().isOnline())
                        factionsPlayer1.getPlayer().sendMessage(Messages.FACTION_PLAYER_KICK.getMessage(factionsPlayer.getName(), player.getName()));
                });
                if (Bukkit.getPlayer(factionsPlayer.getUuid()) != null)
                    Bukkit.getPlayer(factionsPlayer.getUuid()).sendMessage(Messages.FACTION_PLAYER_KICKED.getMessage());
            });
        });

        return Result.SUCCESSFUL;
    }
}
