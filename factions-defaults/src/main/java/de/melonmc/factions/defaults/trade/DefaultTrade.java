package de.melonmc.factions.defaults.trade;
import de.melonmc.factions.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Nico_ND1
 */
public class DefaultTrade extends Trade {

    private final Inventory inventory;

    DefaultTrade(Player leftPlayer, Player rightPlayer) {
        super(leftPlayer, rightPlayer);

        this.inventory = Bukkit.createInventory(null, 4 * 9, "Trade: " + leftPlayer.getName() + " | " + rightPlayer.getName());
        this.inventory.setItem(4, this.createItemStack(Material.EMERALD_BLOCK, "Annehmen"));
        this.inventory.setItem(13, this.createItemStack(Material.IRON_BARDING, "<- " + this.leftPlayer.getName()));
        this.inventory.setItem(22, this.createItemStack(Material.IRON_BARDING, this.rightPlayer.getName() + " ->"));
        this.inventory.setItem(31, this.createItemStack(Material.REDSTONE_BLOCK, "Ablehnen"));
    }

    private ItemStack createItemStack(Material material, String name) {
        final ItemStack itemStack = new ItemStack(material);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
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
        this.leftPlayer.closeInventory();
        this.rightPlayer.sendMessage("Cancel");
        this.rightPlayer.closeInventory();
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
