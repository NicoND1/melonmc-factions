package de.melonmc.bukkit.command.tpa;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class TpaAcceptCommand implements ICommand<Player> {
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

        if (!player.hasMetadata("tpa")) {
            player.sendMessage(Messages.TPA_NO_REQUESTS.getMessage());
            return Result.OTHER;
        }

        final String playerName = args[0];
        final boolean matches = player.getMetadata("tpa").stream()
            .anyMatch(metadataValue -> metadataValue.asString().equalsIgnoreCase(playerName));
        if (matches) {
            final Player target = Bukkit.getPlayer(playerName);
            if (target == null || !target.isOnline()) {
                player.sendMessage(Messages.TPA_REQUEST_PLAYER_OFFLINE.getMessage());
                return Result.OTHER;
            }

            target.teleport(player.getLocation());
            target.sendMessage(Messages.TPA_REQUEST_ACCEPTED.getMessage(player.getName()));
            player.sendMessage(Messages.TPA_REQUEST_ACCEPT.getMessage(target.getName()));
        } else {
            player.sendMessage(Messages.TPA_NO_REQUESTS_FROM.getMessage());
        }

        return Result.SUCCESSFUL;
    }
}
