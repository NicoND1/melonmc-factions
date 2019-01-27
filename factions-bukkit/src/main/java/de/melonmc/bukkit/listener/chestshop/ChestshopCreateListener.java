package de.melonmc.bukkit.listener.chestshop;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.chestshop.Chestshop;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nico_ND1
 */
public class ChestshopCreateListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        final Player player = event.getPlayer();
        if (!event.getLine(0).equalsIgnoreCase("[Shop]")) return;
        if (event.getBlock().getLocation().add(0, -1, 0).getBlock().getType() != Material.CHEST) return;

        final Block block = event.getBlock();
        final Block chestBlock = event.getBlock().getLocation().add(0, -1, 0).getBlock();
        final String itemName = event.getLine(1);
        final Material material;
        if (itemName.matches("(0|[1-9]\\d*)"))
            material = Material.getMaterial(Integer.valueOf(itemName));
        else
            material = Material.getMaterial(itemName.toUpperCase());
        if (material == null) {
            player.sendMessage(Messages.CHESTSHOP_UNKNOWN_MATERIAL.getMessage());
            return;
        }
        final String stringAmount = event.getLine(2);
        final String stringCosts = event.getLine(3);
        if (!stringAmount.matches("(0|[1-9]\\d*)") || !stringCosts.matches("(0|[1-9]\\d*)")) {
            player.sendMessage(Messages.CHESTSHOP_UNKNOWN_INTEGER.getMessage());
            return;
        }
        final int amount = Integer.valueOf(stringAmount);
        if (amount > 64) {
            player.sendMessage(Messages.CHESTSHOP_AMOUNT_TOO_HIGH.getMessage());
            return;
        }
        if (amount < 1) {
            player.sendMessage(Messages.CHESTSHOP_AMOUNT_TOO_LITTLE.getMessage());
            return;
        }
        final int costs = Integer.valueOf(stringCosts);
        final Chestshop chestshop = new Chestshop(
            RandomStringUtils.random(15, true, true),
            new FactionsPlayer(player),
            new ItemStack(material),
            "§c" + material.name(),
            amount,
            costs,
            new ConfigurableLocation(block.getLocation()),
            new ConfigurableLocation(chestBlock.getLocation())
        );

        event.setLine(0, "§7- §8" + player.getName() + " §7-");
        event.setLine(1, chestshop.getDisplayName());
        event.setLine(2, "§cAnzahl§7: §8" + amount);
        event.setLine(3, "§cKosten§7: §8" + costs);

        Factions.getInstance().getDatabaseSaver().saveChestshop(chestshop, () -> player.sendMessage(Messages.CHESTSHOP_CREATED.getMessage()));
    }

}
