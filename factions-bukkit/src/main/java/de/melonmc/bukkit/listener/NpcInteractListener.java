package de.melonmc.bukkit.listener;
import de.bergwerklabs.util.NPCInteractEvent;
import de.melonmc.bukkit.Bootstrapable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Nico_ND1
 */
public class NpcInteractListener implements Listener {

    @EventHandler
    public void onNpcInteract(NPCInteractEvent event) {
        final Player player = event.getPlayer();
        final Bootstrapable bootstrapable = Bootstrapable.getInstance();

        bootstrapable.getNcps().forEach((npcInformation, npc) -> {
            if(npc.equals(event.getInteracted())) {
                player.teleport(npcInformation.getTeleportLocation().toLocation());
                // TODO: Add super cool and fancy animation
            }
        });
    }

}
