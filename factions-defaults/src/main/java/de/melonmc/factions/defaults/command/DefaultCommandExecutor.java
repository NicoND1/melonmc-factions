package de.melonmc.factions.defaults.command;
import de.melonmc.factions.command.AbstractCommandExecutor;
import de.melonmc.factions.command.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
@SuppressWarnings("ALL")
public class DefaultCommandExecutor extends AbstractCommandExecutor {
    public DefaultCommandExecutor(List<ICommand> commands) {
        super(commands);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            this.sendHelpMessage(commandSender, label);
            return false;
        }

        final String subCommandName = args[0];
        final Optional<ICommand> optionalICommand = this.commands.stream()
            .filter(subCommand -> {
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(subCommandName)) return true;
                return subCommand.getName().equalsIgnoreCase(subCommandName);
            }).findAny();

        if (optionalICommand.isPresent())
            optionalICommand.ifPresent(subCommand -> {
                final String newArgs[] = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);

                /*try {
                    // TODO: Update to it won't throw a class cast exception
                    final Class<? extends CommandSender> persistentClass = (Class<? extends CommandSender>) Class.forName(((ParameterizedType) subCommand.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());

                    if (persistentClass == Player.class && !(commandSender instanceof Player)) {
                        commandSender.sendMessage("Du musst ein Spieler sein.");
                        return;
                    }
                    subCommand.onExecute(persistentClass.cast(commandSender), subCommandName, newArgs);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }*/
                subCommand.onExecute((Player) commandSender, subCommandName, newArgs);
            });
        else
            this.sendHelpMessage(commandSender, label);

        return false;
    }

    private void sendHelpMessage(CommandSender commandSender, String label) {
        commandSender.sendMessage("send help message"); // TODO: Build help message
    }
}
