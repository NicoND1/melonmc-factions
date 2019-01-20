package de.melonmc.factions.database;
import de.melonmc.factions.FactionsPlayer;
import de.melonmc.factions.home.Home;

/**
 * @author Nico_ND1
 */
public interface DatabaseSaver {

    void saveHome(Home home);

    void savePlayer(FactionsPlayer factionsPlayer);

}
