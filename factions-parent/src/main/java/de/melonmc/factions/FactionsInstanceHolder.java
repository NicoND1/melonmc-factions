package de.melonmc.factions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

/**
 * @author Nico_ND1
 */
public class FactionsInstanceHolder {

    @Getter @Setter private static Factions factions;
    @Getter @Setter private static Plugin plugin;

}
