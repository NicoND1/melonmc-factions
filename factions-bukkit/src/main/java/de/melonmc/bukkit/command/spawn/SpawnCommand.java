package de.melonmc.bukkit.command.spawn;
import de.melonmc.bukkit.Bootstrapable;
import de.melonmc.factions.Factions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Nico_ND1
 */
public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        final Player player = (Player) commandSender;

        Factions.getInstance().getDatabaseSaver().loadDefaultConfigurations(defaultConfigurations -> player.teleport(defaultConfigurations.getSpawnLocation().toLocation()));

        Bootstrapable.getInstance().getNcps().forEach((npcInformation, npc) -> {
            try {
                npc.appear(player);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return false;
    }
}
