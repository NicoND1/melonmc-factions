package de.melonmc.factions.job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Job {

    @AllArgsConstructor
    public enum Type {

        MINER("Bergbauer", "Bau Stein ab");

        @Getter private final String name;
        @Getter private final String description;

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
