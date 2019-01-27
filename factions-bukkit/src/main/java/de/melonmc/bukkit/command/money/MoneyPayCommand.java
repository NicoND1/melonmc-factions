package de.melonmc.bukkit.command.money;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico_ND1
 */
public class MoneyPayCommand implements ICommand<Player> {

    private final Cache<UUID, Long> timeout = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.SECONDS)
        .build();

    @Override
    public String getName() {
        return "pay";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 2) return Result.WRONG_ARGUMENTS;

        if (this.timeout.getIfPresent(player.getUniqueId()) != null) {
            player.sendMessage(Messages.MONEY_PAY_TIMEOUT.getMessage());
            return Result.OTHER;
        }

        final String playerName = args[0];
        if (playerName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Messages.MONEY_PAY_SELF.getMessage());
            return Result.OTHER;
        }
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

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(player), optionalFactionsPlayer -> {
            if (!optionalFactionsPlayer.isPresent()) {
                player.sendMessage("§cEs kam ein Fehler auf");
                return;
            }

            final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
            if (factionsPlayer.getCoins() < amount) {
                player.sendMessage(Messages.MONEY_PAY_OWN_TOO_LITTLE.getMessage());
                return;
            }

            Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, playerName), optionalFactionsPlayer1 -> {
                if (!optionalFactionsPlayer1.isPresent()) {
                    player.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
                    return;
                }

                final FactionsPlayer targetFactionsPlayer = optionalFactionsPlayer1.get();
                targetFactionsPlayer.setCoins(factionsPlayer.getCoins() + amount);
                factionsPlayer.setCoins(factionsPlayer.getCoins() - amount);

                Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(new FactionsPlayer(
                    targetFactionsPlayer.getUuid(),
                    targetFactionsPlayer.getName(),
                    null,
                    null,
                    amount
                ), () -> {
                    if (targetFactionsPlayer.getPlayer() != null && targetFactionsPlayer.getPlayer().isOnline())
                        targetFactionsPlayer.getPlayer().sendMessage(Messages.MONEY_PAY_RECEIVED.getMessage(player.getName(), amount));
                });
                Factions.getInstance().getDatabaseSaver().incrementPlayerCoins(new FactionsPlayer(
                    factionsPlayer.getUuid(),
                    factionsPlayer.getName(),
                    null,
                    null,
                    -amount
                ), () -> player.sendMessage(Messages.MONEY_PAY_SUCCESS.getMessage(targetFactionsPlayer.getName(), amount)));
            });
        });

        return Result.SUCCESSFUL;
    }
}
