package de.melonmc.bukkit.command.chunk;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.chunk.ChunkManager;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * @author Nico_ND1
 */
public class ChunkInfoCommand implements ICommand<Player> {

    private final BlockFace[] axis = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        final Location location = player.getLocation();
        final ChunkManager chunkManager = Factions.getInstance().getChunkManager();
        final BlockFace blockFace = this.yawToFace(location.getYaw());
        final String[] colors = new String[9];

        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if(!optionalFaction.isPresent()) {
                // SEND MESSAGE BOI
                return;
            }

            this.summarizeColors(optionalFaction.get(), new ClaimableChunk[]{
                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX() - 1, blockFace.getModZ() - 1)),
                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX(), blockFace.getModZ() - 1)),
                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX() + 1, blockFace.getModZ() - 1)),

                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX() - 1, blockFace.getModZ())),
                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX(), blockFace.getModZ())),
                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX() + 1, blockFace.getModZ())),

                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX() - 1, blockFace.getModZ() + 1)),
                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX(), blockFace.getModZ() + 1)),
                chunkManager.getClaimableChunk(player.getWorld().getChunkAt(blockFace.getModX() + 1, blockFace.getModZ() + 1))
            }, strings -> player.sendMessage(Messages.FACTION_CHUNK_INFO.getMessage(strings)));
        });

        /*

        # # #
        # # #
        # # #

        modX-1,modZ-1
        modX-1
        modX-1,modZ+1

        [-1,-1] [0,-1] [1,-1]
        [-1,0]  [0,0]  [1,0]
        [-1,1]  [0,1]  [1,1]

         */

        return Result.SUCCESSFUL;
    }

    private void summarizeColors(Faction ownFaction, ClaimableChunk[] claimableChunks, Consumer<String[]> consumer) {
        final String[] colors = new String[claimableChunks.length];

        for (int i = 0; i < claimableChunks.length; i++) {
            final ClaimableChunk claimableChunk = claimableChunks[i];
            final int i1 = i;
            Factions.getInstance().getChunkManager().getFaction(claimableChunk, optionalFaction -> {
                if (!optionalFaction.isPresent()) {
                    colors[i1] = "§f";
                } else {
                    final Faction faction = optionalFaction.get();
                    if (faction.getName().equalsIgnoreCase(ownFaction.getName()))
                        colors[i1] = "§a";
                    else
                        colors[i1] = "§c";
                }

                if (i1 == claimableChunks.length - 1)
                    consumer.accept(colors);
            });
        }
    }

    private BlockFace yawToFace(float yaw) {
        return axis[Math.round(yaw / 90) & 0x3];
    }
}
