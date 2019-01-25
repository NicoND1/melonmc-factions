package de.melonmc.bukkit.command.home;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.command.CommandSender;

/**
 * @author Nico_ND1
 */
public class HomeRemoveCommand implements ICommand<CommandSender> {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(CommandSender commandSender, String label, String[] args) {
        if (args.length != 2) return Result.WRONG_ARGUMENTS;

        final String playerName = args[0];
        final String homeName = args[1];

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, playerName, null), optionalFactionsPlayer -> {
            if (optionalFactionsPlayer.isPresent() && optionalFactionsPlayer.get().getUuid() != null) {
                final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();

                Factions.getInstance().getDatabaseSaver().findHome(factionsPlayer, homeName, optionalHome -> {
                    if (!optionalHome.isPresent()) {
                        commandSender.sendMessage(Messages.HOME_NOT_FOUND.getMessage());
                        return;
                    }

                    final Home home = optionalHome.get();
                    Factions.getInstance().getDatabaseSaver().deleteHome(home, () -> commandSender.sendMessage(Messages.HOME_DELETED.getMessage()));
                });
            } else {
                commandSender.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
            }
        });

        return null;
    }
}
