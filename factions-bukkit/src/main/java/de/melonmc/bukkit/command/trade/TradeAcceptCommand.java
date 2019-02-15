package de.melonmc.bukkit.command.trade;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.invitation.InvitationManager;
import de.melonmc.factions.invitation.InvitationManager.Key;
import de.melonmc.factions.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class TradeAcceptCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 1) return Result.WRONG_ARGUMENTS;

        final String playerName = args[0];
        final Player target = Bukkit.getPlayer(playerName);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Messages.TRADE_REQUEST_PLAYER_OFFLINE.getMessage());
            return Result.OTHER;
        }
        final InvitationManager invitationManager = Factions.getInstance().getInvitationManager();
        if (invitationManager.isCached(Key.TRADE, player.getUniqueId(), target.getUniqueId())) {
            target.sendMessage(Messages.TRADE_REQUEST_ACCEPTED.getMessage(player.getName()));
            player.sendMessage(Messages.TRADE_REQUEST_ACCEPT.getMessage(target.getName()));

            invitationManager.invalidate(Key.TRADE, player.getUniqueId(), target.getUniqueId());

            final Trade trade = Factions.getInstance().getTradeManager().createTrade(target, player);
            trade.start();
        } else {
            player.sendMessage(Messages.TRADE_NO_REQUESTS_FROM.getMessage());
        }
        return Result.SUCCESSFUL;
    }
}
