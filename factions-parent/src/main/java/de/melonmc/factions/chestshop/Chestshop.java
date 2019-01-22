package de.melonmc.factions.chestshop;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class Chestshop {

    private final String id;
    private final FactionsPlayer owner;
    private final ItemStack itemStack;
    private final String displayName;
    private final int amount;
    private final int costs;
    private final ConfigurableLocation signLocation;
    private final ConfigurableLocation chestLocation;

}
