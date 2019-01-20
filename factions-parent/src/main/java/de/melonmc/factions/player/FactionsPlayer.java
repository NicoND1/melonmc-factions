package de.melonmc.factions.player;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class FactionsPlayer {

    private final UUID uuid;
    private final String name;
    private final Player player;
    private long coins;

}
