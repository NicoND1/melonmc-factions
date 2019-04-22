package de.melonmc.bukkit.command.money;

import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

/**
 * Created on 22.04.2019
 *
 * @author RufixHD
 */

public class MoneyInfoCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 0) return Result.WRONG_ARGUMENTS;

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(player), optionalFactionsPlayer -> {
            if (!optionalFactionsPlayer.isPresent()) {
                player.sendMessage("§cEs kam ein Fehler auf");
                return;
            }

            player.sendMessage(Messages.MONEY_INFO.getMessage(optionalFactionsPlayer.get().getCoins()));
        });
        return Result.SUCCESSFUL;
    }
}
