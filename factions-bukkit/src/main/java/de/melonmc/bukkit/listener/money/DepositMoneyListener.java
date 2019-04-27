package de.melonmc.bukkit.listener.money;

import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created on 27.04.2019
 *
 * @author RufixHD
 */

public class DepositMoneyListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getItem().getType().equals(Material.PAPER) && event.getItem().getItemMeta().getDisplayName().startsWith("§8» §aScheck")){
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                String amounttext = event.getItem().getItemMeta().getDisplayName().split("§2")[1];
                int amount = Integer.parseInt(amounttext);

                ItemStack steck = event.getItem();
                if(steck.getAmount() > 1) {
                    steck.setAmount(steck.getAmount() - 1);
                } else {
                    player.getInventory().setItemInHand(null);
                }

                Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(new FactionsPlayer(
                    null,
                    player.getName(),
                    null,
                    null,
                    +amount
                ), () -> player.sendMessage(Messages.MONEY_DEPOSIT_SUCCESS.getMessage(amount)));
            }
        }
    }
}
