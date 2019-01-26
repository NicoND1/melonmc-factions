package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class FactionBaseCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "base";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"tp"};
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (!optionalFaction.isPresent()) {
                player.sendMessage(Messages.NOT_IN_A_FACTION.getMessage());
                return;
            }

            player.teleport(optionalFaction.get().getLocation().toLocation());
        });

        return Result.SUCCESSFUL;
    }
}
