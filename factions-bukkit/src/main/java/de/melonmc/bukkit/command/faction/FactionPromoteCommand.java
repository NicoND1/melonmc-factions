package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class FactionPromoteCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "promote";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 2) return Result.WRONG_ARGUMENTS;

        final String playerName = args[0];
        final String rankName = args[1];
        Rank rank1 = null;
        try {
            rank1 = Rank.valueOf(rankName);
        } catch (IllegalArgumentException ignored) {
        }
        if (rank1 == null) {
            player.sendMessage(Messages.FACTION_RANK_NOT_FOUND.getMessage());
            return Result.OTHER;
        }
        final Rank rank = rank1;

        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (!optionalFaction.isPresent()) {
                player.sendMessage(Messages.NOT_IN_A_FACTION.getMessage());
                return;
            }

            final Faction faction = optionalFaction.get();
            @SuppressWarnings("OptionalGetWithoutIsPresent") final Entry<FactionsPlayer, Rank> ownEntry = faction.getMembers().entrySet().stream()
                .filter(entry -> entry.getKey().getUuid().equals(player.getUniqueId()))
                .findAny().get();
            if (rank == Rank.ADMIN && ownEntry.getValue() != Rank.ADMIN || ownEntry.getValue() == Rank.PLAYER) {
                player.sendMessage(Messages.FACTION_NOT_ENOUGH_PERMISSIONS.getMessage());
                return;
            }

            Factions.getInstance().getDatabaseSaver().runAction(() -> {
                final Optional<FactionsPlayer> realFactionsPlayer = Factions.getInstance().getDatabaseSaver().findPlayerSync(new FactionsPlayer(null, playerName));
                if (!realFactionsPlayer.isPresent()) {
                    player.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
                    return;
                }

                final Optional<Entry<FactionsPlayer, Rank>> optionalEntry = faction.getMembers().entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equalsIgnoreCase(realFactionsPlayer.get().getName()))
                    .findFirst();
                if (!optionalEntry.isPresent()) {
                    player.sendMessage(Messages.FACTION_TARGET_PLAYER_NOT_FOUND.getMessage(playerName));
                    return;
                }
                final Entry<FactionsPlayer, Rank> entry = optionalEntry.get();
                if (entry.getValue() == Rank.ADMIN || entry.getValue() == ownEntry.getValue()) {
                    player.sendMessage(Messages.FACTION_NOT_ENOUGH_PERMISSIONS.getMessage());
                    return;
                }
                if (entry.getValue() == rank) {
                    player.sendMessage(Messages.FACTION_PLAYER_RANK_NEEDS_CHANGE.getMessage());
                    return;
                }

                entry.setValue(rank);
                Factions.getInstance().getDatabaseSaver().saveFaction(faction, () -> {
                    player.sendMessage(Messages.FACTION_PLAYER_PROMOTE_SUCCESS.getMessage(entry.getKey().getName(), rank.buildPrefix()));
                    faction.broadcast(Messages.FACTION_PLAYER_PROMOTE.getMessage(entry.getKey().getName(), rank.buildPrefix()));
                });
            });
        });

        return Result.SUCCESSFUL;
    }
}
