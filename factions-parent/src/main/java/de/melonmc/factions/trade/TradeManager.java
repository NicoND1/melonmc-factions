package de.melonmc.factions.trade;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Nico_ND1
 */
public interface TradeManager {

    Optional<Player> getTradingPlayer(Player player);

    Optional<Trade> getTrade(Player player);

    Trade createTrade(Player leftPlayer, Player rightPlayer);

}
