package de.melonmc.bukkit.command.spawn;
import de.melonmc.factions.Factions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        final Player player = (Player) commandSender;

        Factions.getInstance().getDatabaseSaver().loadDefaultConfigurations(defaultConfigurations -> player.teleport(defaultConfigurations.getSpawnLocation().toLocation()));
        return false;
    }
}
