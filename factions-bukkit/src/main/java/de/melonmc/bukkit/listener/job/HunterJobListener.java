package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Nico_ND1
 */
public class HunterJobListener extends JobListener {

    public HunterJobListener() {
        super(Type.HUNTER);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        final Player player = event.getEntity().getKiller();
        if (player == null) return;

        this.achieveAction(player);
    }
}
