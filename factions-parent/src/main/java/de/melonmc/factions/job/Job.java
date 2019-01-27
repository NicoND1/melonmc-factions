package de.melonmc.factions.job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Job {

    public enum Type {

    }

    private final Type type;
    private int actions;
    private long totalActions;
    private long level;

    public int incrementActions() {
        this.actions++;
        this.totalActions++;
        return this.actions;
    }
}
