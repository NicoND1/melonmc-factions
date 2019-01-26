package de.melonmc.factions.player;
import de.melonmc.factions.stats.Stats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class FactionsPlayer {

    public static final int HOMES_PER_PLAYER = 3;

    private final UUID uuid;
    private String name;
    private Player player;
    private final Stats stats;
    private long coins;

    public FactionsPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.stats = null;
    }

    public FactionsPlayer(UUID uuid, String name, Stats stats) {
        this.uuid = uuid;
        this.name = name;
        this.stats = stats;
    }

    public FactionsPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.player = player;
        this.stats = null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof FactionsPlayer)) return false;

        return ((FactionsPlayer) other).getName().equalsIgnoreCase(this.name) || ((FactionsPlayer) other).getUuid().equals(this.uuid);
    }

}
