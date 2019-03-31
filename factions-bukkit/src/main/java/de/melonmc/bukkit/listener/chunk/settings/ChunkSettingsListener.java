package de.melonmc.bukkit.listener.chunk.settings;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.chunk.ClaimableChunk;
import de.melonmc.factions.chunk.ClaimableChunk.Flag;
import de.melonmc.factions.faction.Faction;
import de.melonmc.factions.faction.Faction.Rank;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Nico_ND1
 */
public class ChunkSettingsListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().getName().equals("§8» §aChunksettings"))
            return;
        if (event.getCurrentItem() == null) return;

        event.setCancelled(true);

        final int index = event.getSlot() > 8 ? event.getSlot() - 9 : event.getSlot();
        final Flag flag = Flag.values()[index- 20];
        Factions.getInstance().getChunkManager().getFaction(new ClaimableChunk(player.getLocation().getChunk()), optionalFaction -> {
            if (!optionalFaction.isPresent()) { // TODO: Maybe send info message
                player.closeInventory();
                return;
            }

            final Faction faction = optionalFaction.get();
            if (faction.getRank(new FactionsPlayer(player)) == Rank.PLAYER) {
                player.sendMessage(Messages.FACTION_NOT_ENOUGH_PERMISSIONS.getMessage());
                return;
            }

            final ClaimableChunk claimableChunk = Factions.getInstance().getChunkManager().getClaimableChunk(player.getLocation().getChunk());
            claimableChunk.getFlags().put(flag, !claimableChunk.isFlagSet(flag));

            player.closeInventory();

            Bukkit.getScheduler().runTaskLater(Factions.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    Factions.getInstance().getDatabaseSaver().saveFaction(faction, () -> player.performCommand("chunk settings"));
                }
            },1);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        Factions.getInstance().getChunkManager().getFaction(new ClaimableChunk(player.getLocation().getChunk()), optionalFaction -> {
            if (optionalFaction.isPresent()) {
                final Faction faction = optionalFaction.get();
                if (faction.getRank(new FactionsPlayer(player)) == Rank.UNKNOWN) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.CHUNK_SETTINGS_BUILD_CANCEL.getMessage());
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Factions.getInstance().getChunkManager().getFaction(new ClaimableChunk(player.getLocation().getChunk()), optionalFaction -> {
            if (optionalFaction.isPresent()) {
                final Faction faction = optionalFaction.get();
                if (faction.getRank(new FactionsPlayer(player)) == Rank.UNKNOWN) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.CHUNK_SETTINGS_BUILD_CANCEL.getMessage());
                }
            }
        });
    }
}
