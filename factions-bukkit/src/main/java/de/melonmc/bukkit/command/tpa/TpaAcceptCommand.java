package de.melonmc.bukkit.command.tpa;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.invitation.InvitationManager;
import de.melonmc.factions.invitation.InvitationManager.Key;
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

        final String playerName = args[0];
        final Player target = Bukkit.getPlayer(playerName);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Messages.TPA_REQUEST_PLAYER_OFFLINE.getMessage());
            return Result.OTHER;
        }
        final InvitationManager invitationManager = Factions.getInstance().getInvitationManager();
        if (invitationManager.isCached(Key.TPA, player.getUniqueId(), target.getUniqueId())) {
            target.teleport(player.getLocation());
            target.sendMessage(Messages.TPA_REQUEST_ACCEPTED.getMessage(player.getName()));
            player.sendMessage(Messages.TPA_REQUEST_ACCEPT.getMessage(target.getName()));

            invitationManager.invalidate(Key.TPA, player.getUniqueId(), target.getUniqueId());
        } else {
            player.sendMessage(Messages.TPA_NO_REQUESTS_FROM.getMessage());
        }

        return Result.SUCCESSFUL;
    }
}
