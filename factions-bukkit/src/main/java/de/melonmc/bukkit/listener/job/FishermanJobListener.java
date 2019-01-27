package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * @author Nico_ND1
 */
public class FishermanJobListener extends JobListener {

    public FishermanJobListener() {
        super(Type.FISHERMAN);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        final Player player = event.getPlayer();
        if (event.getCaught() == null) return;

        this.achieveAction(player);
    }
}
