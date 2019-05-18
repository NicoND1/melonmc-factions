package de.melonmc.bukkit.command.job;
import de.MelonMC.RufixHD.SystemAPI.APIs.InventarAPI;
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

            InventarAPI.createAnimatedInv(45, "§8» §aJobs", player);
            InventarAPI.setItem(player, 19, null);
            InventarAPI.setItem(player, 20, null);
            InventarAPI.setItem(player, 21, null);
            InventarAPI.setItem(player, 22, null);
            InventarAPI.setItem(player, 23, null);
            InventarAPI.setItem(player, 24, null);
            InventarAPI.setItem(player, 25, null);

            for (Type type : Type.values()) {
                final boolean hasJob = jobPlayer.getJobs().stream().anyMatch(job -> job.getType() == type);
                final ItemStack itemStack = new ItemStack(type.getMaterial());
                final ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName((hasJob ? "§8» §a" : "§8» §c") + type.getName());

                final List<String> lore = new ArrayList<>();
                if (hasJob) {
                    final Job job = jobPlayer.getJobs().stream().filter(job1 -> job1.getType() == type).findAny().get();
                    lore.add("§7Level§8: §e" + job.getLevel());
                    if(job.getLevel() <= 9) {
                        lore.add("§7Aktionen bis zum Levelup§8: §e" + (JobPlayer.NEEDED_ACTION_FOR_LEVEL - job.getActions()));
                    } else {
                        lore.add("§7Maximales Level §aerreicht§8!");
                    }
                    lore.add(" ");
                }
                lore.addAll(Arrays.asList(type.getDescription().split("\n")));
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);

                player.getOpenInventory().getTopInventory().addItem(itemStack);
            }
        });

        return Result.SUCCESSFUL;
    }
}
