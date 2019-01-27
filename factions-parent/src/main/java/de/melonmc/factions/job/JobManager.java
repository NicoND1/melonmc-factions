package de.melonmc.factions.job;
import de.melonmc.factions.job.Job.Type;

/**
 * @author Nico_ND1
 */
public interface JobManager {

    void registerJobListener(JobListener jobListener);

    Type[] getTypes();

    void achieveAction(JobPlayer jobPlayer, Job job);

}
