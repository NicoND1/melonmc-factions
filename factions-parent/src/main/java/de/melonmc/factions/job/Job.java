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

        WOODCUTTER("Holfzfäller", "Bau Rohholz ab", Material.WOOD),
        MINER("Bergbauer", "Bau Stein ab", Material.IRON_PICKAXE),
        FARMER("Bauer", "Pflege oder töte Tiere", Material.SHEARS),
        HUNTER("Jäger", "Töte Mobs", Material.BOW),
        WIZARD("Zauberer", "Braue Tränke", Material.POTION),
        FISHERMAN("Fischer", "Angel", Material.FISHING_ROD),
        SMITH("Schmied", "Schmiede Items", Material.ANVIL);

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
