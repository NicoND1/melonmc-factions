package de.melonmc.factions;
import lombok.Getter;

/**
 * @author Nico_ND1
 */
public class FactionsInstanceHolder {

    @Getter private static Factions factions;

    public static void setFactions(Factions factions) {
        FactionsInstanceHolder.factions = factions;
    }

}
