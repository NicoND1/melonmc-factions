package de.melonmc.bukkit.listener.chestshop;
import de.melonmc.factions.Factions;
import de.melonmc.factions.chestshop.Chestshop;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class ChestshopInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!event.hasBlock() || event.getClickedBlock().getType() != Material.CHEST) return;
        final Block signBlock = event.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
        if (signBlock.getType() != Material.SIGN && signBlock.getType() != Material.SIGN_POST && signBlock.getType() != Material.WALL_SIGN)
            return;

        final Block block = event.getClickedBlock();
        final List<Chestshop> chestshops = Factions.getInstance().getDatabaseSaver().loadChestshopsSync(new FactionsPlayer(player));
        final Optional<Chestshop> optionalChestshop = chestshops.stream()
            .filter(chestshop -> chestshop.getChestLocation().toLocation().getBlock().equals(block))
            .findFirst();

        event.setCancelled(!optionalChestshop.isPresent());
    }

}
