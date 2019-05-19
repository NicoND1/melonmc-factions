package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.stats.Stats;
import de.melonmc.factions.util.ConfigurableLocation;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

import static de.melonmc.factions.faction.Faction.MAX_TAG_LENGTH;
import static de.melonmc.factions.faction.Faction.MIN_TAG_LENGTH;

/**
 * @author Nico_ND1
 */
public class FactionCreateCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"c", "add"};
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 2) return Result.WRONG_ARGUMENTS;

        final String factionName = args[0];
        final String factionTag = args[1];
        if (factionTag.length() < MIN_TAG_LENGTH || factionTag.length() > MAX_TAG_LENGTH) {
            player.sendMessage(Messages.FACTION_INVALID_TAG.getMessage(MIN_TAG_LENGTH, MAX_TAG_LENGTH));
            return Result.OTHER;
        }

        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (optionalFaction.isPresent()) {
                player.sendMessage(Messages.ALREADY_IN_FACTION.getMessage());
                return;
            }

            final Faction faction = new Faction(factionName, factionTag);
            Factions.getInstance().getDatabaseSaver().loadFaction(faction, optionalFaction1 -> {
                if (optionalFaction1.isPresent()) {
                    player.sendMessage(Messages.FACTION_ALREADY_EXISTS.getMessage());
                    return;
                }

                final Faction realFaction = new Faction(new HashMap<FactionsPlayer, Rank>() {{
                    this.put(new FactionsPlayer(player), Rank.ADMIN);
                }}, new ArrayList<>(),
                    factionName,
                    factionTag,
                    new Stats(0, 0),
                    new ArrayList<>(),
                    new ConfigurableLocation(player.getLocation()),
                    0);

                Factions.getInstance().getDatabaseSaver().saveFaction(realFaction, () -> player.sendMessage(Messages.FACTION_CREATED.getMessage(factionName, factionTag)));
            });
        });

        return Result.SUCCESSFUL;
    }
}
