package de.melonmc.bukkit.listener.chestshop;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.chestshop.Chestshop;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nico_ND1
 */
public class ChestshopBuyListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        final Block signBlock = event.getClickedBlock();
        if (signBlock.getType() != Material.SIGN && signBlock.getType() != Material.SIGN_POST && signBlock.getType() != Material.WALL_SIGN)
            return;

        Factions.getInstance().getDatabaseSaver().findChestshop(new ConfigurableLocation(signBlock.getLocation()), optionalChestshop -> {
            if (!optionalChestshop.isPresent()) return;

            final Chestshop chestshop = optionalChestshop.get();
            if (chestshop.getOwner().getUuid().equals(player.getUniqueId())) {
                player.sendMessage(Messages.CHESTSHOP_BUY_OWN.getMessage());
                return;
            }

            final Block chestBlock = chestshop.getChestLocation().toLocation().getBlock();
            final Chest chest = (Chest) chestBlock.getState();
            if (!chest.getBlockInventory().containsAtLeast(chestshop.getItemStack(), chestshop.getAmount())) {
                player.sendMessage(Messages.CHESTSHOP_EMPTY.getMessage());
                return;
            }

            boolean hasFreeSlot = false;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    hasFreeSlot = true;
                    break;
                }
                if (itemStack.getType() != chestshop.getItemStack().getType()) continue;
                if (itemStack.hasItemMeta()) continue;
                if (!itemStack.getData().equals(chestshop.getItemStack().getData())) continue;
                if (itemStack.getAmount() + chestshop.getAmount() <= 64) {
                    hasFreeSlot = true;
                    break;
                }
            }
            if (!hasFreeSlot) {
                player.sendMessage(Messages.CHESTSHOP_INVENTORY_FULL.getMessage());
                return;
            }

            Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(player), optionalFactionsPlayer -> {
                if (!optionalFactionsPlayer.isPresent()) {
                    player.sendMessage("§cEs kam ein Fehler auf.");
                    return;
                }

                final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
                if (factionsPlayer.getCoins() < chestshop.getAmount()) {
                    player.sendMessage(Messages.CHESTSHOP_TOO_LITTLE_MONEY.getMessage());
                    return;
                }

                int amount = chestshop.getAmount();
                for (ItemStack itemStack : chest.getBlockInventory().getContents()) {
                    if (itemStack == null) continue;
                    if (itemStack.getType() != chestshop.getItemStack().getType()) continue;
                    if (itemStack.hasItemMeta()) continue;
                    if (!itemStack.getData().equals(chestshop.getItemStack().getData())) continue;

                    if (itemStack.getAmount() > amount) {
                        itemStack.setAmount(itemStack.getAmount() - amount);
                        amount = 0;
                        break;
                    } else {
                        amount -= itemStack.getAmount();
                        chest.getBlockInventory().setItem(chest.getBlockInventory().first(itemStack), new ItemStack(Material.AIR));
                    }
                }
                if (amount != 0) {
                    player.sendMessage("§cEs kam ein Fehler auf.");
                    throw new RuntimeException("Amount does not equal 0");
                }

                amount = chestshop.getAmount();
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null) continue;
                    if (itemStack.getType() != chestshop.getItemStack().getType()) continue;
                    if (itemStack.hasItemMeta()) continue;
                    if (!itemStack.getData().equals(chestshop.getItemStack().getData())) continue;

                    if (itemStack.getAmount() + amount > 64) {
                        amount -= 64 - itemStack.getAmount();
                        itemStack.setAmount(64);
                    } else {
                        itemStack.setAmount(itemStack.getAmount() + amount);
                        amount = 0;
                    }
                }

                if (amount > 0)
                    player.getInventory().addItem(new ItemStack(chestshop.getItemStack().getType(), amount));

                Factions.getInstance().getDatabaseSaver().findPlayer(chestshop.getOwner(), optionalFactionsPlayer1 -> {
                    if (!optionalFactionsPlayer1.isPresent()) {
                        player.sendMessage("§cEs kam ein Fehler auf.");
                        return;
                    }

                    final FactionsPlayer owner = optionalFactionsPlayer1.get();
                    owner.setCoins(chestshop.getCosts());
                    factionsPlayer.setCoins(-chestshop.getCosts());

                    Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(owner, () -> {
                        if (owner.getPlayer() != null && owner.getPlayer().isOnline())
                            owner.getPlayer().sendMessage(Messages.CHESTSHOP_PLAYER_BOUGHT.getMessage(
                                player.getName(),
                                chestshop.getAmount() + " " + chestshop.getDisplayName(),
                                chestshop.getCosts()
                            ));
                    });
                    Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(factionsPlayer, () -> player.sendMessage(Messages.CHESTSHOP_PLAYER_BOUGHT_SUCCESS.getMessage(
                        chestshop.getAmount() + " " + chestshop.getDisplayName(),
                        chestshop.getCosts(),
                        owner.getName()
                    )));
                });
            });
        });
    }

}
