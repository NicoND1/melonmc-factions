package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author Nico_ND1
 */
public class WoodcutterJobListener extends JobListener {

    public WoodcutterJobListener() {
        super(Type.WOODCUTTER);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (event.getBlock().getType() != Material.WOOD) return;

        this.achieveAction(player);
    }
}
