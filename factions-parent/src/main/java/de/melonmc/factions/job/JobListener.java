package de.melonmc.factions.job;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.player.FactionsPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public abstract class JobListener implements Listener {

    protected final Type type;

    public void getJobs(Player player, Consumer<List<Job>> consumer) {
        Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
            if (optionalJobPlayer.isPresent())
                consumer.accept(optionalJobPlayer.get().getJobs());
            else
                consumer.accept(new ArrayList<>());
        });
    }

    public void getJob(Player player, Consumer<Optional<Job>> consumer) {
        Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
            if (!optionalJobPlayer.isPresent()) {
                consumer.accept(Optional.empty());
                return;
            }

            consumer.accept(optionalJobPlayer.get().getJobs().stream()
                .filter(job -> job.getType() == this.type)
                .findFirst());
        });
    }

    public void achieveAction(Player player) {
        this.getJob(player, optionalJob -> {
            if (!optionalJob.isPresent()) return;

            final Job job = optionalJob.get();

            Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
                if (!optionalJobPlayer.isPresent()) {
                    player.sendMessage("§cEs kam ein Fehler auf.");
                    return;
                }
                Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(new FactionsPlayer(
                    null,
                    player.getName(),
                    null,
                    null,
                    job.getCoinDiff()
                ), () -> player.updateInventory());

                Factions.getInstance().getJobManager().achieveAction(optionalJobPlayer.get(), job);
            });
        });
    }

}
