package de.melonmc.factions.chestshop;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class Chestshop {

    private final FactionsPlayer owner;
    private final Material material;
    private final String displayName;
    private final int amount;
    private final int costs;
    private final ConfigurableLocation signLocation;
    private final ConfigurableLocation chestLocation;

}
