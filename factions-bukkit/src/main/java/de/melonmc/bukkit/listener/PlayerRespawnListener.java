package de.melonmc.bukkit.listener;

import de.melonmc.bukkit.Bootstrapable;
import de.melonmc.factions.Factions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.lang.reflect.InvocationTargetException;

/**
 * Created on 06.05.2019
 *
 * @author RufixHD
 */

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public static void onRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();

        Bukkit.getScheduler().runTaskLater(Factions.getPlugin(), new Runnable() {
            @Override
            public void run() {
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
            }
        }, 10);
    }
}
