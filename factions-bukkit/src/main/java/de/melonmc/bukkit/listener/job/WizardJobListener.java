package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

/**
 * @author Nico_ND1
 */
public class WizardJobListener extends JobListener {

    public WizardJobListener() {
        super(Type.WIZARD);
    }

    @EventHandler
    public void onBrew(EnchantItemEvent event) {
        final Player player = event.getEnchanter();

        this.achieveAction(player);
    }
}
