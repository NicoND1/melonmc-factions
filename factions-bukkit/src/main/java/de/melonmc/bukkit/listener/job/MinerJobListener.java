package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.Factions;
import de.melonmc.factions.job.Job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author Nico_ND1
 */
public class MinerJobListener extends JobListener {
    public MinerJobListener() {
        super(Type.MINER);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (event.getBlock().getType() != Material.STONE) return;

        this.getJob(player, optionalJob -> {
            if (!optionalJob.isPresent()) return;

            final Job job = optionalJob.get();
            Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
                if (!optionalJobPlayer.isPresent()) {
                    player.sendMessage("§cEs kam ein Fehler auf.");
                    return;
                }
                Factions.getInstance().getJobManager().achieveAction(optionalJobPlayer.get(), job);
            });
        });
    }
}
