package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Nico_ND1
 */
public class FactionListCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"members"};
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (!optionalFaction.isPresent()) {
                player.sendMessage(Messages.NOT_IN_A_FACTION.getMessage());
                return;
            }

            final Faction faction = optionalFaction.get();
            final Map<Rank, List<FactionsPlayer>> map = new LinkedHashMap<>();
            faction.getMembers().forEach((factionsPlayer, rank) -> {
                final List<FactionsPlayer> list = map.getOrDefault(rank, new ArrayList<>());
                list.add(factionsPlayer);

                map.put(rank, list);
            });

            final StringBuilder stringBuilder = new StringBuilder(Messages.FACTION_LIST_PREFIX.getMessage());
            Factions.getInstance().getDatabaseSaver().runAction(() -> {
                for (int i = 0; i < Rank.values().length; i++) {
                    final Rank rank = Rank.values()[i];
                    final List<FactionsPlayer> list = map.get(rank);
                    stringBuilder.append(rank.buildPrefix()).append("§8: ").append(rank.getColorCode());
                    if (list == null || list.isEmpty()) {
                        stringBuilder.append("§c/");
                        continue;
                    }

                    for (int i1 = 0; i < list.size(); i++) {
                        final FactionsPlayer factionsPlayer = list.get(i1);
                        String name = factionsPlayer.getName();
                        if (name == null) {
                            final Optional<FactionsPlayer> optionalFactionsPlayer = Factions.getInstance().getDatabaseSaver().findPlayerSync(factionsPlayer);
                            if (optionalFactionsPlayer.isPresent()) name = optionalFactionsPlayer.get().getName();
                        }

                        stringBuilder.append(name);
                        if (i1 != list.size() - 1) stringBuilder.append("§8, ").append(rank.getColorCode());
                    }

                    stringBuilder.append("\n");
                }

                player.sendMessage(stringBuilder.toString());
            });
        });

        return Result.SUCCESSFUL;
    }
}
