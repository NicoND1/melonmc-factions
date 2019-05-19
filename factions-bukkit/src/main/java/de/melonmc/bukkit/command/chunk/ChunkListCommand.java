package de.melonmc.bukkit.command.chunk;

import de.melonmc.factions.Factions;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

/**
 * Created on 19.05.2019
 *
 * @author RufixHD
 */

public class ChunkListCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        Factions.getInstance().getDatabaseSaver().findFaction(new FactionsPlayer(player), optionalFaction -> {
            if (!optionalFaction.isPresent()) return;

            player.sendMessage("§8§m--------------§8[ §a§lChunkList §8]§8§m--------------");
            player.sendMessage("");

                optionalFaction.get().getChunks().forEach((chunk) -> {
                    player.sendMessage("§8» §2X§8: §a" + chunk.getChunk().getX() + " §8× §2Z§8: §a" + chunk.getChunk().getZ());
                });
            });

            player.sendMessage("");
            player.sendMessage("§8§m----------------------------------------");

        return Result.SUCCESSFUL;
    }
}
