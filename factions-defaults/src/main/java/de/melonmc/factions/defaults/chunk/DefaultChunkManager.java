package de.melonmc.factions.defaults.chunk;
import de.melonmc.factions.Factions;
import de.melonmc.factions.chunk.ChunkManager;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.faction.Faction;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Nico_ND1
 */
public class DefaultChunkManager implements ChunkManager {

    private final List<ClaimableChunk> claimableChunks = new ArrayList<>();

    @Override
    public ClaimableChunk getClaimableChunk(Chunk chunk) {
        return this.claimableChunks.stream()
            .filter(claimableChunk -> claimableChunk.getChunk().equals(chunk))
            .findFirst()
            .orElse(this.createAndRegisterClaimableChunk(chunk));
    }

    private ClaimableChunk createAndRegisterClaimableChunk(Chunk chunk) {
        final ClaimableChunk claimableChunk = new ClaimableChunk(chunk);
        this.claimableChunks.add(claimableChunk);

        return claimableChunk;
    }

    @Override
    public void getFaction(ClaimableChunk claimableChunk, Consumer<Optional<Faction>> consumer) {
        Factions.getInstance().getDatabaseSaver().findFactionByClaimableChunk(claimableChunk, consumer);
    }

    @Override
    public void claimChunk(Faction faction, ClaimableChunk claimableChunk, Runnable runnable) {
        faction.getChunks().add(claimableChunk);
        Factions.getInstance().getDatabaseSaver().saveFaction(faction, runnable);
    }

    @Override
    public void unclaimChunk(Faction faction, ClaimableChunk claimableChunk, Runnable runnable) {
        faction.getChunks().removeIf(claimableChunk1 -> claimableChunk.getChunk().equals(claimableChunk1.getChunk()));
        Factions.getInstance().getDatabaseSaver().saveFaction(faction, runnable);
    }
}
