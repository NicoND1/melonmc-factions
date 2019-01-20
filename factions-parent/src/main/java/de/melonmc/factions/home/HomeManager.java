package de.melonmc.factions.home;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;

import java.util.List;

/**
 * @author Nico_ND1
 */
public interface HomeManager {

    Home getHome(String name, FactionsPlayer factionsPlayer);

    List<Home> getHomes(FactionsPlayer factionsPlayer);

    Home createHome(String name, FactionsPlayer factionsPlayer, ConfigurableLocation location);



}
