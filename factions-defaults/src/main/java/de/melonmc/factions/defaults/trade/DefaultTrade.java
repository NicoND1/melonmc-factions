package de.melonmc.factions.defaults.trade;
import de.melonmc.factions.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nico_ND1
 */
public class DefaultTrade extends Trade {

    private final Inventory inventory;

    public DefaultTrade(Player leftPlayer, Player rightPlayer) {
        super(leftPlayer, rightPlayer);

        this.inventory = Bukkit.createInventory(null, 4 * 9, "Trade: " + leftPlayer.getName() + " | " + rightPlayer.getName());
    }

    @Override
    public void start() {
        this.leftPlayer.openInventory(this.inventory);
        this.rightPlayer.openInventory(this.inventory);
    }

    @Override
    public void updateInventory() {
        this.updateItems(this.leftPlayer);
        this.updateItems(this.rightPlayer);
    }

    private void updateItems(Player player) {
        final int[] slots = this.getSlots(player);
        final ItemStack[] items = this.getItems(player);
        for (int i = 0; i < slots.length; i++) {
            final int slot = slots[i];
            final ItemStack itemStack = items[i];

            this.inventory.setItem(slot, itemStack);
        }
    }

    @Override
    public boolean putItem(Player player, int slot, ItemStack itemStack) {
        boolean matches = false;
        for (int i : this.getSlots(player))
            if (i == slot) matches = true;
        if (!matches) return false;

        final int index = this.getIndex(this.getSlots(player), slot);
        this.getItems(player)[index] = itemStack;

        this.updateInventory();

        return true;
    }

    @Override
    public void finish() {
        this.leftPlayer.sendMessage("Finish");
        this.rightPlayer.sendMessage("Finish");
    }

    @Override
    public void cancel() {
        this.leftPlayer.sendMessage("Cancel");
        this.rightPlayer.sendMessage("Cancel");
    }

    private ItemStack[] getItems(Player player) {
        return this.leftPlayer.equals(player) ? this.leftItems : this.rightItems;
    }

    private int[] getSlots(Player player) {
        return this.leftPlayer.equals(player) ? Trade.LEFT_ITEMS : Trade.RIGHT_ITEMS;
    }

    private int getIndex(int[] slots, int i) {
        int index = -1;
        for (int slot = 0; slot < slots.length; slot++)
            if (slots[slot] == i) index = slot;
        return index;
    }
}
