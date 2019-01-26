package de.melonmc.bukkit.command.chunk;
import de.melonmc.bukkit.listener.chunk.settings.ChunkSettingsListener;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.chunk.ClaimableChunk.Flag;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Nico_ND1
 */
public class ChunkSettingsCommand implements ICommand<Player> {

    public ChunkSettingsCommand() {
        Bukkit.getPluginManager().registerEvents(new ChunkSettingsListener(), Factions.getPlugin());
    }

    @Override
    public String getName() {
        return "settings";
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
            final ClaimableChunk claimableChunk = Factions.getInstance().getChunkManager().getClaimableChunk(player.getLocation().getChunk());
            Factions.getInstance().getChunkManager().getFaction(claimableChunk, optionalFaction1 -> {
                if (!optionalFaction1.isPresent()) {
                    player.sendMessage(Messages.FACTION_CHUNK_NOT_CLAIMED.getMessage());
                    return;
                }

                final Faction faction1 = optionalFaction1.get();
                if (!faction.getName().equalsIgnoreCase(faction1.getName())) {
                    player.sendMessage(Messages.FACTION_CHUNK_NOT_OWN.getMessage());
                    return;
                }

                final Inventory inventory = Bukkit.createInventory(null, 36, "Chunk Settings");
                for (int i = 0; i < Flag.values().length; i++) {
                    final Flag flag = Flag.values()[i];

                    inventory.setItem(i, this.createItem(flag.getMaterial(), flag.name(), (short) 0));
                    inventory.setItem(i + 9, this.createItem(Material.INK_SACK, "An / Aus", (short) (claimableChunk.isFlagSet(flag) ? 10 : 1)));
                }

                player.openInventory(inventory);
            });
        });

        return Result.SUCCESSFUL;
    }

    private ItemStack createItem(Material material, String name, short data) {
        final ItemStack itemStack = new ItemStack(material, 1, data);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
