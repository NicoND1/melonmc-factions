package de.melonmc.bukkit.command.home;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;
import de.melonmc.factions.util.ConfigurableLocation;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class HomeSetCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"set", "update"};
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length != 1) return Result.WRONG_ARGUMENTS;
        final String homeName = args[0];

        Factions.getInstance().getDatabaseSaver().findHomes(new FactionsPlayer(player), homes -> {
            final Optional<Home> optionalHome = homes.stream()
                .filter(home -> home.getName().equalsIgnoreCase(homeName))
                .findAny();

            if (optionalHome.isPresent()) {
                final Home home = optionalHome.get();
                home.setLocation(new ConfigurableLocation(player.getLocation()));

                Factions.getInstance().getDatabaseSaver().saveHome(home, () -> player.sendMessage(Messages.HOME_UPDATED.getMessage(home.getName())));
            } else {
                if (homes.size() >= FactionsPlayer.HOMES_PER_PLAYER) {
                    player.sendMessage(Messages.TOO_MANY_HOMES.getMessage(FactionsPlayer.HOMES_PER_PLAYER));
                    return;
                }

                final Home home = new Home(new FactionsPlayer(player), homeName, new ConfigurableLocation(player.getLocation()));

                Factions.getInstance().getDatabaseSaver().saveHome(home, () -> player.sendMessage(Messages.HOME_SET.getMessage(home.getName())));
            }
        });
        return Result.SUCCESSFUL;
    }
}
