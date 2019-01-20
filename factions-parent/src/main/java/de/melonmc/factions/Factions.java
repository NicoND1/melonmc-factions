package de.melonmc.factions;
import de.melonmc.factions.database.DatabaseSaver;
import de.melonmc.factions.home.HomeManager;

/**
 * @author Nico_ND1
 */
public interface Factions {

    HomeManager getHomeManager();

    DatabaseSaver getDatabaseSaver();

    static Factions getInstance() {
        return FactionsInstanceHolder.getFactions();
    }

}
