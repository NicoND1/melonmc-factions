package de.melonmc.bukkit.command.home;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.AbstractCommandExecutor;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.home.Home;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class HomeCommand extends AbstractCommandExecutor {

    private final AbstractCommandExecutor parentCommandExecutor;

    public HomeCommand(List<ICommand> commands, AbstractCommandExecutor parentCommandExecutor) {
        super("home", commands);
        this.parentCommandExecutor = parentCommandExecutor;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        final Player player = (Player) sender;

        if (args.length == 1) {
            final String subCommandName = args[0];
            final Optional<ICommand> optionalICommand = this.commands.stream()
                .filter(subCommand -> {
                    for (String alias : subCommand.getAliases())
                        if (alias.equalsIgnoreCase(subCommandName)) return true;
                    return subCommand.getName().equalsIgnoreCase(subCommandName);
                }).findAny();

            if (!optionalICommand.isPresent()) {
                Factions.getInstance().getDatabaseSaver().findHomes(new FactionsPlayer(player), homes -> {
                    final Optional<Home> homeOptional = homes.stream()
                        .filter(home -> home.getName().equalsIgnoreCase(subCommandName))
                        .findAny();

                    if (homeOptional.isPresent()) {
                        player.teleport(homeOptional.get().getLocation().toLocation());
                    } else {
                        player.sendMessage(Messages.HOME_NOT_FOUND.getMessage());
                    }
                });
                return true;
            }
        }

        return this.parentCommandExecutor.onCommand(sender, command, label, args);
    }
}
