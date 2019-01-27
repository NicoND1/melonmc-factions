package de.melonmc.factions.defaults.job;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.job.Job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import de.melonmc.factions.job.JobManager;
import de.melonmc.factions.job.JobPlayer;
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

    @Override
    public void achieveAction(JobPlayer jobPlayer, Job job) {
        job.incrementActions();
        if (job.getActions() >= JobPlayer.NEEDED_ACTION_FOR_LEVEL) {
            job.setActions(0);
            job.setLevel(job.getLevel() + 1);

            Bukkit.getPlayer(jobPlayer.getUuid()).sendMessage(Messages.JOB_LEVEL_ACHIEVED.getMessage(job.getType().getName(), job.getLevel()));
        }
        job.setCoinDiff(job.getCoinDiff() + (int) (JobPlayer.MONEY_PER_ACTION * (JobPlayer.MONEY_MULTIPLIER * job.getLevel() + 1)));
    }
}
