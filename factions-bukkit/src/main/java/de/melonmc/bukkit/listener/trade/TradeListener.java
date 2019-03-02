package de.melonmc.bukkit.listener.trade;
import de.melonmc.factions.Factions;
import de.melonmc.factions.trade.Trade;
import de.melonmc.factions.trade.TradeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class TradeListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        if (event.getInventory() == null || event.getInventory().getName() == null) return;
        if (!event.getInventory().getName().startsWith("Trade")) return;

        final TradeManager tradeManager = Factions.getInstance().getTradeManager();
        tradeManager.getTrade(player).ifPresent(Trade::cancel);
        // TODO: Send message that player has closed the inventory
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        if (inventory == null || inventory.getName() == null) return;
        if (!inventory.getName().startsWith("Trade")) return;
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName())
            return;
        final TradeManager tradeManager = Factions.getInstance().getTradeManager();
        final Optional<Trade> optionalTrade = tradeManager.getTrade(player);
        if (!optionalTrade.isPresent()) return;
        final Trade trade = optionalTrade.get();
        if (!this.onItemMove(player, trade, event.getCurrentItem(), event.getSlot()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getInventory();
        if (inventory == null || inventory.getName() == null) return;
        if (!inventory.getName().startsWith("Trade")) return;
        final TradeManager tradeManager = Factions.getInstance().getTradeManager();
        final Optional<Trade> optionalTrade = tradeManager.getTrade(player);
        if (!optionalTrade.isPresent()) return;
        final Trade trade = optionalTrade.get();
        event.getNewItems().forEach((integer, itemStack) -> {
            if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) return;
            if (!this.onItemMove(player, trade, itemStack, integer)) event.setCancelled(true);
        });
    }

    private boolean onItemMove(Player player, Trade trade, ItemStack itemStack, int slot) {
        final boolean success = trade.putItem(player, slot, itemStack);
        return success;
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        if (Factions.getInstance().getTradeManager().getTrade(player).isPresent()) event.setCancelled(true);
    }

}
