package de.melonmc.bukkit.command.home;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class HomeRemoveCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 1) return Result.WRONG_ARGUMENTS;

        final String homeName = args[0];
        Factions.getInstance().getDatabaseSaver().findPlayerUuid(player.getUniqueId().toString(), optionalFactionsPlayer -> {
            if (optionalFactionsPlayer.isPresent()) {
                final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();

                Factions.getInstance().getDatabaseSaver().findHome(factionsPlayer, homeName, optionalHome -> {
                    if (!optionalHome.isPresent()) {
                        player.sendMessage(Messages.HOME_NOT_FOUND.getMessage());
                        return;
                    }

                    final Home home = optionalHome.get();
                    Factions.getInstance().getDatabaseSaver().deleteHome(home, () -> player.sendMessage(Messages.HOME_DELETED.getMessage()));
                });
            }
        });
        return Result.SUCCESSFUL;
    }
}
