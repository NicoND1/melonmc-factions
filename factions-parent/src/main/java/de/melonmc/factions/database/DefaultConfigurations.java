package de.melonmc.factions.database;
import de.melonmc.factions.util.ConfigurableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class DefaultConfigurations {

    private final ConfigurableLocation spawnLocation;
    private final List<NpcInformation> npcInformations;

}
