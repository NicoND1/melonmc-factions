package de.melonmc.factions.database;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Nico_ND1
 */
public interface DatabaseSaver {

    void saveHome(Home home, Runnable runnable);

    void deleteHome(Home home, Runnable runnable);

    void findHome(FactionsPlayer factionsPlayer, Consumer<Optional<Home>> consumer);

    void findHomes(FactionsPlayer factionsPlayer, Consumer<Optional<List<Home>>> consumer);

    void savePlayer(FactionsPlayer factionsPlayer, Runnable runnable);

    void deletePlayer(FactionsPlayer factionsPlayer, Runnable runnable);

    /**
     * Searches for a {@link FactionsPlayer} in the database.
     *
     * @param currentFactionsPlayer The {@link FactionsPlayer} which has to contain either {@link FactionsPlayer#name}
     *                              or {@link FactionsPlayer#uuid}
     * @param consumer              The {@link Consumer<Optional<FactionsPlayer>>} which will be called when the player was found
     */
    void findPlayer(FactionsPlayer currentFactionsPlayer, Consumer<Optional<FactionsPlayer>> consumer);

    void saveFaction(Faction faction, Runnable runnable);

    void deleteFaction(Faction faction, Runnable runnable);

    void loadFaction(Faction faction, Consumer<Optional<Faction>> consumer);

}
