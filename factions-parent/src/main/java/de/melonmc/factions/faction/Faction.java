package de.melonmc.factions.faction;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.stats.Stats;
import de.melonmc.factions.util.ConfigurableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class Faction {

    public static final int CHUNKS_PER_PLAYER = 10;
    public static final int MAX_TAG_LENGTH = 5;
    public static final int MIN_TAG_LENGTH = 3;
    public static final int MAX_INVITES = 5;

    @AllArgsConstructor
    public enum Rank {

        ADMIN("§c", "Admin"), MODERATOR("§a", "Mod"), PLAYER("§7", "Spieler");

        @Getter private final String colorCode;
        @Getter private final String prefix;

        public String buildPrefix() {
            return this.colorCode + this.prefix;
        }

    }

    private final Map<FactionsPlayer, Rank> members;
    private final List<FactionsPlayer> invitedPlayers;
    private final String name;
    private final String tag;
    private final Stats stats;
    private final List<ClaimableChunk> chunks;
    private ConfigurableLocation location;
    private long eloPoints;

    public Faction(String name, String tag) {
        this.name = name;
        this.tag = tag;
        this.members = null;
        this.invitedPlayers = null;
        this.stats = null;
        this.chunks = null;
    }

    public void incrementEloPoints(int increment) {
        this.eloPoints += increment;
    }

    public void decrementEloPoints(int decrement) {
        this.eloPoints -= decrement;
    }

    public void broadcast(String message, Rank... ranks) {
        this.members.entrySet().stream()
            .filter(entry -> {
                for (Rank rank : ranks) {
                    if (rank == entry.getValue())
                        return true;
                }
                return ranks.length == 0;
            }).forEach(entry -> {
            final FactionsPlayer factionsPlayer = entry.getKey();
            if (Bukkit.getPlayer(factionsPlayer.getUuid()) != null && factionsPlayer.getPlayer() == null)
                factionsPlayer.setPlayer(Bukkit.getPlayer(factionsPlayer.getUuid()));
            if (factionsPlayer.getPlayer() == null || !factionsPlayer.getPlayer().isOnline()) return;

            factionsPlayer.getPlayer().sendMessage(message);
        });
    }

}
