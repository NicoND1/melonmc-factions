package de.melonmc.factions.defaults.command;
import de.melonmc.factions.Factions;
import de.melonmc.factions.command.AbstractCommandExecutor;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.command.Tab;
import de.melonmc.factions.command.TabInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Nico_ND1
 */
@SuppressWarnings("ALL")
public class DefaultCommandExecutor extends AbstractCommandExecutor implements TabCompleter {

    private final List<TabInfo> tabInfos = new ArrayList<>();

    public DefaultCommandExecutor(String commandName, List<ICommand> commands) {
        super(commandName, commands);

        Factions.getPlugin().getServer().getPluginCommand(commandName).setExecutor(this);
        Factions.getPlugin().getServer().getPluginCommand(commandName).setTabCompleter(this);

        commands.forEach(command -> {
            for (Method method : command.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tab.class))
                    this.tabInfos.add(new TabInfo(command, method.getAnnotation(Tab.class), method));
            }
        });
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
                if (subCommand.getPermission() != null && !commandSender.hasPermission(subCommand.getPermission()))
                    return false;
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
        // TODO: Build help message

        if (label.equals("home")) {
            commandSender.sendMessage("§8§m--------------§8[ §a§lHomeSystem §8]§8§m--------------");
            commandSender.sendMessage("");
            commandSender.sendMessage("§a/home set NAME §8● §7Homepunkt setzen");
            commandSender.sendMessage("§a/home NAME §8● §7Zu einem Homepunkt teleportieren");
            commandSender.sendMessage("§a/home list §8● §7homepunkte anzeigen");
            commandSender.sendMessage("");
            commandSender.sendMessage("§8§m----------------------------------------");
            return;
        }

        commandSender.sendMessage("Hilfenachricht wurde noch nicht erstellt!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return null;

        final List<String> list = new ArrayList<>();
        final Optional<TabInfo> optionalTabInfo = this.tabInfos.stream()
            .filter(tabInfo -> {
                if (tabInfo.getICommand().getPermission() != null && !sender.hasPermission(tabInfo.getICommand().getPermission()))
                    return false;
                for (String alias : tabInfo.getICommand().getAliases())
                    if (alias.equalsIgnoreCase(args[0])) return true;
                return tabInfo.getICommand().getName().equalsIgnoreCase(args[0]);
            }).filter(tabInfo -> tabInfo.getTab().value() == args.length - 2)
            .findFirst();
        optionalTabInfo.ifPresent(tabInfo -> {
            final Method method = tabInfo.getMethod();
            final String newArgs[] = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);

            try {
                final List<String> list1 = (List<String>) method.invoke(tabInfo.getICommand(), (Player) sender, label, newArgs);
                list.addAll(list1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        return list;
    }
}
