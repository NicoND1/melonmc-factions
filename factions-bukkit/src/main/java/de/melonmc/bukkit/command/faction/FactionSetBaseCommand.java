package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;
import org.bukkit.entity.Player;

import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class FactionSetBaseCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "setbase";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
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
                player.sendMessage("§cEs kam ein Fehler auf.");
                return;
            }
            final Entry<FactionsPlayer, Rank> entry = optionalEntry.get();
            if (entry.getValue() == Rank.PLAYER) {
                player.sendMessage(Messages.FACTION_NOT_ENOUGH_PERMISSIONS.getMessage());
                return;
            }

            faction.setLocation(new ConfigurableLocation(player.getLocation()));
            Factions.getInstance().getDatabaseSaver().saveFaction(faction, () -> player.sendMessage(Messages.FACTION_PLAYER_BASE_SET.getMessage()));
        });

        return Result.SUCCESSFUL;
    }
}
