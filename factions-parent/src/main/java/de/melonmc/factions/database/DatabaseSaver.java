package de.melonmc.factions.database;
import de.melonmc.factions.chestshop.Chestshop;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico_ND1
 */
public interface DatabaseSaver {

    void runAction(Runnable runnable);

    void notifyPlayerQuit(UUID uuid);

    void saveHome(Home home, Runnable runnable);

    void deleteHome(Home home, Runnable runnable);

    void findHome(FactionsPlayer factionsPlayer, String name, Consumer<Optional<Home>> consumer);

    void findHomes(FactionsPlayer factionsPlayer, Consumer<List<Home>> consumer);

    void savePlayer(FactionsPlayer factionsPlayer, Runnable runnable);

    void incrementPlayerCoins(FactionsPlayer factionsPlayer, Runnable runnable);

    void deletePlayer(FactionsPlayer factionsPlayer, Runnable runnable);

    /**
     * Searches for a {@link FactionsPlayer} in the database.
     *
     * @param currentFactionsPlayer The {@link FactionsPlayer} which has to contain either {@link FactionsPlayer#name}
     *                              or {@link FactionsPlayer#uuid}
     * @param consumer              The {@link Consumer<Optional<FactionsPlayer>>} which will be called when the player was found
     */
    void findPlayer(FactionsPlayer currentFactionsPlayer, Consumer<Optional<FactionsPlayer>> consumer);

    Optional<FactionsPlayer> findPlayerSync(FactionsPlayer currentFactionsPlayer);

    void findPlayerUuid(String name, Consumer<Optional<FactionsPlayer>> consumer);

    void saveFaction(Faction faction, Runnable runnable);

    void saveFactionInvites(Faction faction, Runnable runnable);

    void deleteFaction(Faction faction, Runnable runnable);

    void loadAllFactions();

    void loadFaction(Faction faction, Consumer<Optional<Faction>> consumer);

    void loadTopTenFactions(Consumer<List<Faction>> consumer);

    /**
     * Searches for all invites from {@code factionsPlayer} and gives back a list of names from
     * {@link Faction factions} that invited him.
     *
     * @param factionsPlayer
     * @param consumer
     */
    void findFactionInvites(FactionsPlayer factionsPlayer, Consumer<List<String>> consumer);

    List<String> findFactionInvitesSync(FactionsPlayer factionsPlayer);

    void findFactionByClaimableChunk(ClaimableChunk claimableChunk, Consumer<Optional<Faction>> consumer);

    void findFaction(FactionsPlayer factionsPlayer, Consumer<Optional<Faction>> consumer);

    void saveDefaultConfigurations(DefaultConfigurations defaultConfigurations, Runnable runnable);

    void loadDefaultConfigurations(Consumer<DefaultConfigurations> consumer);

    void saveChestshop(Chestshop chestshop, Runnable runnable);

    void deleteChestshop(FactionsPlayer factionsPlayer, String id, Runnable runnable);

    void findChestshop(ConfigurableLocation anyLocation, Consumer<Optional<Chestshop>> consumer);

    void loadChestshops(FactionsPlayer factionsPlayer, Consumer<List<Chestshop>> consumer);

    List<Chestshop> loadChestshopsSync(FactionsPlayer factionsPlayer);

}
