package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
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
        if (!event.getClickedInventory().getName().equalsIgnoreCase("§8» §aJobs")) return;
        event.setCancelled(true);
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName())
            return;

        final int index = event.getSlot();
        final int Types = Type.values().length + 19;
        if (index > Types) return;
        final Type type = Type.values()[index - 19];
        Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
            if (!optionalJobPlayer.isPresent()) {
                player.sendMessage("§cEs kam ein Fehler auf.");
                return;
            }
            final JobPlayer jobPlayer = optionalJobPlayer.get();
            boolean change = false;
            if (event.isLeftClick() && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8» §a")) {
                    jobPlayer.getJobs().removeIf(job -> job.getType() == type);
                    change = true;
            } else if (event.isRightClick() && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8» §c")) {
                if (jobPlayer.getJobs().size() <= 3) {
                    jobPlayer.getJobs().add(new Job(type, 0, 0, 1, 0));
                    change = true;
                } else {
                    player.sendMessage(Messages.JOB_MAX.getMessage());
                }
            }

            if (change) player.performCommand("jobs list");
        });
    }
}
