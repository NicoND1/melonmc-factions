package de.melonmc.factions.faction;
import de.melonmc.factions.chunks.ClaimableChunk;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.stats.Stats;
import de.melonmc.factions.util.ConfigurableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class Faction {

    public static final int CHUNKS_PER_PLAYER = 10;

    public enum Rank {

        ADMIN, MODERATOR, PLAYER;

    }

    private final Map<FactionsPlayer, Rank> members;
    private final List<FactionsPlayer> invitedPlayers;
    private final String name;
    private final String tag;
    private final Stats stats;
    private final List<ClaimableChunk> chunks;
    private ConfigurableLocation location;
    private long eloPoints;

    public void incrementEloPoints(int increment) {
        this.eloPoints += increment;
    }

    public void decrementEloPoints(int decrement) {
        this.eloPoints -= decrement;
    }

}
