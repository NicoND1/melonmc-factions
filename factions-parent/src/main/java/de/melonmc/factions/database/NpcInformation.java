package de.melonmc.factions.database;
import de.melonmc.factions.util.ConfigurableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class NpcInformation {

    private final String nameHeader;
    private final String nameFooter;
    private ConfigurableLocation location;
    private ConfigurableLocation teleportLocation;

}
