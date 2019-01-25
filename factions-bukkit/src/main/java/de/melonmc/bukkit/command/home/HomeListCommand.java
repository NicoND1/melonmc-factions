package de.melonmc.bukkit.command.home;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Nico_ND1
 */
public class HomeListCommand implements ICommand<Player> {

    public static final String LIST_OTHER_PERMISSION = "factions.command.home.list.other";

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        if (args.length == 1 && player.hasPermission(LIST_OTHER_PERMISSION)) {
            final String name = args[0];

            Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, name, null), optionalPlayer -> {
                if (!optionalPlayer.isPresent()) {
                    // TODO: Send message that player wasn't found
                    return;
                }

                Factions.getInstance().getDatabaseSaver().findHomes(new FactionsPlayer(optionalPlayer.get().getUuid(), name, null), homes -> {
                    if (homes.isEmpty()) {
                        player.sendMessage(Messages.HOMES_EMPTY_OTHER.getMessage());
                        return;
                    }

                    this.listHomes(player, homes);
                });
            });
        } else {
            Factions.getInstance().getDatabaseSaver().findHomes(new FactionsPlayer(player), homes -> {
                if (homes.isEmpty()) {
                    player.sendMessage(Messages.HOMES_EMPTY.getMessage());
                    return;
                }

                this.listHomes(player, homes);
            });
        }

        return Result.SUCCESSFUL;
    }

    private void listHomes(Player player, List<Home> list) {
        // TODO: List homes
    }
}
