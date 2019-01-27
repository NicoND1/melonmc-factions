package de.melonmc.factions.job;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class JobPlayer {

    public static final double MONEY_MULTIPLIER = 1.1; // +10%
    public static final int MONEY_PER_ACTION = 10;
    public static final int NEEDED_ACTION_FOR_LEVEL = 50;

    private final UUID uuid;
    private int actions;
    private long totalActions;
    private long level;

    public int incrementActions() {
        this.actions++;
        this.totalActions++;
        return this.actions;
    }

}
