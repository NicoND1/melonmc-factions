package de.melonmc.factions.defaults;
import de.melonmc.factions.Factions;
import de.melonmc.factions.database.DatabaseSaver;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class DefaultFactions implements Factions {

    private final DatabaseSaver databaseSaver;
}
