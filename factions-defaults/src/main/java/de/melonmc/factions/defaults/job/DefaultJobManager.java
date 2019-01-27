package de.melonmc.factions.defaults.job;
import de.melonmc.factions.Factions;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import de.melonmc.factions.job.JobManager;
import org.bukkit.Bukkit;

/**
 * @author Nico_ND1
 */
public class DefaultJobManager implements JobManager {
    @Override
    public void registerJobListener(JobListener jobListener) {
        Bukkit.getPluginManager().registerEvents(jobListener, Factions.getPlugin());
    }

    @Override
    public Type[] getTypes() {
        return Type.values();
    }
}
