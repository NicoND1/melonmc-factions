package de.melonmc.bukkit;
import de.bergwerklabs.util.NPC;
import de.melonmc.bukkit.listener.SpawnWorldListener;
import de.melonmc.factions.Factions;
import de.melonmc.factions.IBootstrapable;
import de.melonmc.factions.database.NpcInformation;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Nico_ND1
 */
@Data
public class Bootstrapable implements IBootstrapable {

    @Getter private static Bootstrapable instance;
    private final Map<NpcInformation, NPC> ncps = new HashMap<>();

    @Override
    public void onStart() {
        instance = this;

        Factions.getInstance().getDatabaseSaver().loadDefaultConfigurations(defaultConfigurations -> {
            defaultConfigurations.getNpcInformations().forEach(npcInformation -> {
                try {
                    final NPC npc = new NPC(
                        npcInformation.getNameHeader(),
                        npcInformation.getNameFooter(),
                        true,
                        true,
                        npcInformation.getLocation().toLocation()
                    );
                    npc.spawn();
                } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

            final World world = Bukkit.getWorld(defaultConfigurations.getSpawnLocation().getWorldName());
            if (world == null) {
                Factions.getPlugin().getLogger().log(Level.WARNING, "Spawn location is null, can't apply flags!");
                return;
            }

            Bukkit.getPluginManager().registerEvents(new SpawnWorldListener(world), Factions.getPlugin());
        });
    }

    @Override
    public void onStop() {
        this.ncps.forEach((npcInformation, npc) -> {
            try {
                npc.despawn();
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
