package de.melonmc.bukkit.listener.job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobListener;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

/**
 * @author Nico_ND1
 */
public class FarmerJobListener extends JobListener {

    public FarmerJobListener() {
        super(Type.FARMER);
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        final Player player = event.getPlayer();

        this.achieveAction(player);
    }

    @EventHandler
    public void onMilkCow(PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();

        if (event.getRightClicked() instanceof Cow && player.getItemInHand() != null && player.getItemInHand().getType() == Material.BUCKET)
            this.achieveAction(player);
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        final Player player = (Player) event.getOwner();

        this.achieveAction(player);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        final Player player = event.getEntity().getKiller();
        if (player == null) return;

        switch (event.getEntityType()) {
            case CHICKEN:
            case HORSE:
            case MUSHROOM_COW:
            case COW:
            case PIG:
            case RABBIT:
            case SHEEP:
                this.achieveAction(player);
                break;
        }
    }
}
