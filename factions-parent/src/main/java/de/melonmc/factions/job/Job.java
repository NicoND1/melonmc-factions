package de.melonmc.factions.job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Job {

    @AllArgsConstructor
    public enum Type {

        MINER("Bergbauer", "Bau Stein ab", Material.IRON_PICKAXE);

        @Getter private final String name;
        @Getter private final String description;
        @Getter private final Material material;

    }

    private final Type type;
    private int actions;
    private long totalActions;
    private long level;
    private int coinDiff;

    public void incrementActions() {
        this.actions++;
        this.totalActions++;
    }
}
