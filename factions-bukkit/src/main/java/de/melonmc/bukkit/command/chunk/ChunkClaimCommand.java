package de.melonmc.bukkit.command.chunk;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.chunk.ChunkManager;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class ChunkClaimCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (!optionalFaction.isPresent()) {
                player.sendMessage(Messages.NOT_IN_A_FACTION.getMessage());
                return;
            }

            final Faction faction = optionalFaction.get();
            if (faction.getRank(new FactionsPlayer(player)) == Rank.PLAYER) {
                player.sendMessage(Messages.FACTION_NOT_ENOUGH_PERMISSIONS.getMessage());
                return;
            }

            final ChunkManager chunkManager = Factions.getInstance().getChunkManager();
            final ClaimableChunk claimableChunk = chunkManager.getClaimableChunk(player.getLocation().getChunk());
            chunkManager.getFaction(claimableChunk, optionalFaction1 -> {
                if (optionalFaction1.isPresent()) {
                    player.sendMessage(Messages.FACTION_CHUNK_ALREADY_CLAIMED.getMessage());
                    return;
                }

                chunkManager.claimChunk(faction, claimableChunk, () -> player.sendMessage(Messages.FACTION_CHUNK_CLAIMED.getMessage()));
            });
        });

        return Result.SUCCESSFUL;
    }
}
