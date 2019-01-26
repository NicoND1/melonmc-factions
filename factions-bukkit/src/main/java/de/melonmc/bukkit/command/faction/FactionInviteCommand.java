package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class FactionInviteCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 1) return Result.WRONG_ARGUMENTS;

        final String playerName = args[0];
        if (playerName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Messages.FACTION_PLAYER_INVITE_SELF.getMessage());
            return Result.SUCCESSFUL;
        }

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, playerName, null), optionalFactionsPlayer -> {
            if (!optionalFactionsPlayer.isPresent()) {
                player.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
                return;
            }

            final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
            Factions.getInstance().getDatabaseSaver().findFaction(factionsPlayer, optionalFaction -> {
                if (optionalFaction.isPresent()) {
                    player.sendMessage(Messages.FACTION_PLAYER_IN_FACTION.getMessage());
                    return;
                }

                Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFactions -> {
                    if (!optionalFactions.isPresent()) {
                        player.sendMessage(Messages.NOT_IN_A_FACTION.getMessage());
                        return;
                    }

                    final Faction faction = optionalFactions.get();
                    Factions.getInstance().getDatabaseSaver().findFactionInvites(factionsPlayer, factionNames -> {
                        if (factionNames.contains(faction.getName())) {
                            player.sendMessage(Messages.FACTION_PLAYER_ALREADY_INVITED.getMessage());
                            return;
                        }

                        faction.getInvitedPlayers().add(factionsPlayer);

                        final Player targetPlayer = Bukkit.getPlayer(factionsPlayer.getUuid());
                        if (targetPlayer != null)
                            targetPlayer.sendMessage(Messages.FACTION_PLAYER_INVITE_RECEIVED.getMessage(faction.getName(), faction.getTag()));

                        Factions.getInstance().getDatabaseSaver().saveFactionInvites(faction, () -> player.sendMessage(Messages.FACTION_PLAYER_INVITED.getMessage()));
                    });
                });
            });
        });

        return Result.SUCCESSFUL;
    }
}
