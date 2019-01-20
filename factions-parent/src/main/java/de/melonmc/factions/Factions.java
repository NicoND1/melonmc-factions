package de.melonmc.factions;
import de.melonmc.factions.database.DatabaseSaver;

/**
 * @author Nico_ND1
 */
public interface Factions {

    DatabaseSaver getDatabaseSaver();

    static Factions getInstance() {
        return FactionsInstanceHolder.getFactions();
    }

}
