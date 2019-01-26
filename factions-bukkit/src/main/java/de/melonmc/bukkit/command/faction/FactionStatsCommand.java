package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class FactionStatsCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length == 1) {
            final String factionName = args[0];

            Factions.getInstance().getDatabaseSaver().loadFaction(new Faction(factionName, null), optionalFaction -> this.sendStatsMessage(player, optionalFaction, Messages.FACTION_NOT_FOUND.getMessage()));
        } else {
            Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> this.sendStatsMessage(player, optionalFaction, Messages.NOT_IN_A_FACTION.getMessage()));
        }

        return Result.SUCCESSFUL;
    }

    private void sendStatsMessage(Player player, Optional<Faction> optionalFaction, String message) {
        if (!optionalFaction.isPresent()) {
            player.sendMessage(message);
            return;
        }

        final Faction faction = optionalFaction.get();
        player.sendMessage(Messages.FACTION_STATS.getMessage(
            faction.getName(),
            faction.getStats().getKills(), faction.getStats().getKills() == 1 ? "" : "s",
            faction.getStats().getDeaths(), faction.getStats().getDeaths() == 1 ? "" : "e"
        ));
    }
}
