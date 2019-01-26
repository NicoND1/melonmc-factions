package de.melonmc.factions.chunk;
import de.melonmc.factions.faction.Faction;
import org.bukkit.Chunk;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Nico_ND1
 */
public interface ChunkManager {

    ClaimableChunk getClaimableChunk(Chunk chunk);

    void getFaction(ClaimableChunk claimableChunk, Consumer<Optional<Faction>> consumer);

    void claimChunk(Faction faction, ClaimableChunk claimableChunk, Runnable runnable);

    void unclaimChunk(Faction faction, ClaimableChunk claimableChunk, Runnable runnable);

}
