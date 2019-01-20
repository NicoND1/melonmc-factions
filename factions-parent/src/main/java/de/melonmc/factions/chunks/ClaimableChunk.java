package de.melonmc.factions.chunks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.Map;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class ClaimableChunk {

    @AllArgsConstructor
    public enum Flag {

        PVP(true);

        @Getter private final boolean defaultValue;

    }

    private final int x;
    private final int z;
    private final String worldName;
    private final Map<Flag, Boolean> flags;
    private Chunk chunk;

    public Chunk getChunk() {
        if (this.chunk != null) return this.chunk;

        this.chunk = Bukkit.getWorld(this.worldName).getChunkAt(this.x, this.z);

        return this.chunk;
    }

    public boolean isFlagSet(Flag flag) {
        return this.flags.getOrDefault(flag, flag.defaultValue);
    }

}
