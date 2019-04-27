package de.melonmc.bukkit.command.money;

import de.MelonMC.RufixHD.SystemAPI.APIs.ItemAPI;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 27.04.2019
 *
 * @author RufixHD
 */

public class MoneyWithdrawCommand implements ICommand<Player> {

    public static ArrayList<String> lore = new ArrayList<>();

    @Override
    public String getName() {
        return "withdraw";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 1) return Result.WRONG_ARGUMENTS;

        final String stringAmount = args[0];
        if (!stringAmount.matches("(0|[1-9]\\d*)")) {
            player.sendMessage(Messages.MONEY_UNKNOWN_INTEGER.getMessage());
            return Result.OTHER;
        }
        final int amount = Integer.valueOf(stringAmount);
        if (amount <= 0) {
            player.sendMessage(Messages.MONEY_PAY_TOO_LITTLE.getMessage());
            return Result.OTHER;
        }

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(player), optionalFactionsPlayer -> {
            if (!optionalFactionsPlayer.isPresent()) {
                player.sendMessage("§cEs kam ein Fehler auf");
                return;
            }

            if (optionalFactionsPlayer.get().getCoins() < amount) {
                player.sendMessage(Messages.MONEY_PAY_OWN_TOO_LITTLE.getMessage());
                return;
            }

            Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(new FactionsPlayer(
                null,
                player.getName(),
                null,
                null,
                -amount
            ), () -> player.sendMessage(Messages.MONEY_WHITDRAW_SUCCESS.getMessage(amount)));

            lore.clear();
            lore.add(" ");
            lore.add("§8➥ §7Recktsklicke um §e" + amount + " §7auf dein Konto zu zahlen§8.");
            lore.add(" ");

            player.getInventory().addItem(ItemAPI.Item(Material.PAPER, 1, "§8» §aScheck §8● §2" + amount, lore));
        });
        return Result.SUCCESSFUL;
    }
}
