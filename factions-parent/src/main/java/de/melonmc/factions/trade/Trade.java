package de.melonmc.factions.trade;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nico_ND1
 */
@RequiredArgsConstructor
@Data
public abstract class Trade {

    protected static final int[] LEFT_ITEMS = {1};
    protected static final int[] RIGHT_ITEMS = {1};
    protected final ItemStack[] leftItems = new ItemStack[LEFT_ITEMS.length];
    protected final ItemStack[] rightItems = new ItemStack[RIGHT_ITEMS.length];
    protected final Player leftPlayer;
    protected final Player rightPlayer;

    public abstract void start();

    public abstract void updateInventory();

    public abstract boolean putItem(Player player, int slot, ItemStack itemStack);

    public abstract void finish();

    public abstract void cancel();

}
