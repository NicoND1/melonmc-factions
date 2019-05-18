package de.melonmc.factions.job;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class JobPlayer {

    public static final double MONEY_MULTIPLIER = .01;
    public static final int MONEY_PER_ACTION = 1;
    public static final int NEEDED_ACTION_FOR_LEVEL = 50;

    private final UUID uuid;
    private final List<Job> jobs;

    public JobPlayer(UUID uuid) {
        this.uuid = uuid;
        this.jobs = null;
    }
}
