package de.melonmc.factions.defaults.trade;
import de.melonmc.factions.trade.Trade;
import de.melonmc.factions.trade.TradeManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class DefaultTradeManager implements TradeManager {

    private final List<Trade> trades = new ArrayList<>();

    @Override
    public Optional<Player> getTradingPlayer(Player player) {
        final Optional<Trade> optionalTrade = this.getTrade(player);
        return optionalTrade.map(trade -> trade.getLeftPlayer().equals(player) ? trade.getRightPlayer() : trade.getLeftPlayer());
    }

    @Override
    public Optional<Trade> getTrade(Player player) {
        return this.trades.stream()
            .filter(trade -> trade.getRightPlayer().equals(player) || trade.getLeftPlayer().equals(player))
            .findFirst();
    }

    @Override
    public Trade createTrade(Player leftPlayer, Player rightPlayer) {
        return new DefaultTrade(leftPlayer, rightPlayer);
    }
}
