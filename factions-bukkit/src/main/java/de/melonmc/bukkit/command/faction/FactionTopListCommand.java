package de.melonmc.bukkit.command.faction;
import de.melonmc.factions.Factions;
import de.melonmc.factions.command.ICommand;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class FactionTopListCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"toplist"};
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) { // TODO: Add real message
        Factions.getInstance().getDatabaseSaver().loadTopTenFactions(factions -> factions.forEach(faction -> player.sendMessage(faction.getName())));

        return Result.SUCCESSFUL;
    }
}
