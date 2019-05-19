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

        player.sendMessage("§8§m--------------§8[ §a§lTopClans §8]§8§m--------------");
        player.sendMessage("");

        Factions.getInstance().getDatabaseSaver().loadTopTenFactions(factions -> factions.forEach(faction -> player.sendMessage("§8» §2" + faction.getName() + " §8× §a" + faction.getEloPoints())));

        player.sendMessage("");
        player.sendMessage("§8§m----------------------------------------");

        return Result.SUCCESSFUL;
    }
}
