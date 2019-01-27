package de.melonmc.bukkit.command.job;
import de.melonmc.factions.Factions;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.job.Job;
import de.melonmc.factions.job.Job.Type;
import de.melonmc.factions.job.JobPlayer;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nico_ND1
 */
public class JobsCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Result onExecute(Player player, String label, String[] args) {
        Factions.getInstance().getDatabaseSaver().loadJobPlayer(new FactionsPlayer(player), optionalJobPlayer -> {
            if (!optionalJobPlayer.isPresent()) {
                player.sendMessage("§cEs kam ein Fehler auf.");
                return;
            }

            final JobPlayer jobPlayer = optionalJobPlayer.get();
            final int invSize = Type.values().length % 9 == 0 ?
                (Type.values().length / 9) * 9 :
                (Type.values().length / 9) * 9 + 9;
            final Inventory inventory = Bukkit.createInventory(null, invSize, "Jobs");
            for (Type type : Type.values()) {
                final boolean hasJob = jobPlayer.getJobs().stream().anyMatch(job -> job.getType() == type);
                final ItemStack itemStack = new ItemStack(type.getMaterial());
                final ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName((hasJob ? "§a" : "§c") + type.getName());

                final List<String> lore = new ArrayList<>();
                if (hasJob) {
                    final Job job = jobPlayer.getJobs().stream().filter(job1 -> job1.getType() == type).findAny().get();
                    lore.add("§7Level§8: §e" + job.getLevel());
                    lore.add("§7Aktionen bis zum Levelup§8: §e" + (JobPlayer.NEEDED_ACTION_FOR_LEVEL - job.getActions()));
                    lore.add(" ");
                }
                lore.addAll(Arrays.asList(type.getDescription().split("\n")));
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);

                inventory.addItem(itemStack);
            }

            player.openInventory(inventory);
        });

        return Result.SUCCESSFUL;
    }
}
