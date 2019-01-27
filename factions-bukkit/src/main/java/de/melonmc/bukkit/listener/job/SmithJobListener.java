package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;

/**
 * @author Nico_ND1
 */
public class SmithJobListener extends JobListener {

    public SmithJobListener() {
        super(Type.SMITH);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (!(event.getClickedInventory() instanceof AnvilInventory)) return;

        if (event.getSlot() == 2 && event.getCurrentItem() != null) // Output
            this.achieveAction(player);
    }
}
