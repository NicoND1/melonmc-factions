package de.melonmc.factions.home;
import de.melonmc.factions.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class Home {

    private final FactionsPlayer factionsPlayer;
    private final String name;
    private ConfigurableLocation location;

}
