package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.command.Tab;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nico_ND1
 */
public class FactionDenyCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "deny";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 1) return Result.WRONG_ARGUMENTS;

        final String factionName = args[0];
        Factions.getInstance().getDatabaseSaver().findFactionInvites(new FactionsPlayer(player), factionNames -> {
            if (!factionNames.contains(factionName)) {
                player.sendMessage(Messages.FACTION_NOT_INVITED.getMessage());
                return;
            }

            Factions.getInstance().getDatabaseSaver().loadFaction(new Faction(factionName, null), optionalFaction -> {
                if (!optionalFaction.isPresent()) {
                    player.sendMessage(Messages.FACTION_INVITED_FACTION_NOT_FOUND.getMessage());
                    return;
                }

                final Faction faction = optionalFaction.get();
                faction.getInvitedPlayers().removeIf(factionsPlayer -> factionsPlayer.getUuid().equals(player.getUniqueId()));

                Factions.getInstance().getDatabaseSaver().saveFactionInvites(faction, () -> player.sendMessage(Messages.FACTION_INVITE_DECLINED.getMessage()));
            });
        });

        return Result.SUCCESSFUL;
    }

    @Tab(0)
    public List<String> onTab(Player player, String label, String[] args) {
        final List<String> factionNames = Factions.getInstance().getDatabaseSaver().findFactionInvitesSync(new FactionsPlayer(player));
        return factionNames.stream()
            .filter(string -> string.startsWith(args[0]))
            .map(String::toString).collect(Collectors.toList());
    }
}
