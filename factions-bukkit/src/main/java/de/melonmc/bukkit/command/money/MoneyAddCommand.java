package de.melonmc.bukkit.command.money;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class MoneyAddCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPermission() {
        return "melonmc.retrosurvival.coinsystem";
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 2) return Result.WRONG_ARGUMENTS;

        final String playerName = args[0];
        final String stringAmount = args[1];
        if (!stringAmount.matches("(0|[1-9]\\d*)")) {
            player.sendMessage(Messages.MONEY_UNKNOWN_INTEGER.getMessage());
            return Result.OTHER;
        }
        final int amount = Integer.valueOf(stringAmount);
        if (amount <= 0) {
            player.sendMessage(Messages.MONEY_PAY_TOO_LITTLE.getMessage());
            return Result.OTHER;
        }

        Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(new FactionsPlayer(
            null,
            playerName,
            null,
            null,
            amount
        ), () -> player.sendMessage(Messages.MONEY_ADD_SUCCESS.getMessage(playerName, amount)));

        return Result.SUCCESSFUL;
    }
}
