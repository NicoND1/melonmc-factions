package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.Factions;
import de.melonmc.factions.job.Job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobPlayer;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Nico_ND1
 */
public class JobSettingsListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().getName().equalsIgnoreCase("Jobs")) return;
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName())
            return;

        final int index = event.getSlot();
        if (index > Type.values().length) return;
        event.setCancelled(true);

        final Type type = Type.values()[index];
        Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
            if (!optionalJobPlayer.isPresent()) {
                player.sendMessage("§cEs kam ein Fehler auf.");
                return;
            }

            final JobPlayer jobPlayer = optionalJobPlayer.get();
            boolean change = false;
            if (event.isLeftClick() && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) {
                jobPlayer.getJobs().removeIf(job -> job.getType() == type);
                change = true;
            } else if (event.isRightClick() && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§c")) {
                jobPlayer.getJobs().add(new Job(type, 0, 0, 1, 0));
                change = true;
            }

            if (change) player.performCommand("jobs list");
        });
    }

}
