package de.melonmc.factions;
/**
 * @author Nico_ND1
 */
public interface Factions {



    static Factions getInstance() {
        return FactionsInstanceHolder.getFactions();
    }

}
