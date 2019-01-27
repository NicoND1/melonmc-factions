package de.melonmc.bukkit.listener.chestshop;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.chestshop.Chestshop;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class ChestshopDestroyListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (event.getBlock().getType() != Material.CHEST &&
            (event.getBlock().getType() != Material.SIGN && event.getBlock().getType() != Material.SIGN_POST && event.getBlock().getType() != Material.WALL_SIGN))
            return;

        Factions.getInstance().getDatabaseSaver().loadChestshops(new FactionsPlayer(player), chestshops -> {
            chestshops.forEach(chestshop -> System.out.println(chestshop.getChestLocation().toLocation().getBlock().equals(event.getBlock())
                || chestshop.getSignLocation().toLocation().getBlock().equals(event.getBlock())));
            final Optional<Chestshop> optionalChestshop = chestshops.stream()
                .filter(chestshop -> chestshop.getChestLocation().toLocation().getBlock().equals(event.getBlock())
                    || chestshop.getSignLocation().toLocation().getBlock().equals(event.getBlock()))
                .findFirst();
            if (!optionalChestshop.isPresent()) return;

            final Chestshop chestshop = optionalChestshop.get();
            Factions.getInstance().getDatabaseSaver().deleteChestshop(new FactionsPlayer(player), chestshop.getId(), () -> player.sendMessage(Messages.CHESTSHOP_DELETED.getMessage()));
        });
    }

}
