package de.melonmc.bukkit.command.tpa;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.melonmc.factions.Factions;
import de.melonmc.factions.Messages;
import de.melonmc.factions.command.AbstractCommandExecutor;
import de.melonmc.factions.command.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico_ND1
 */
public class TpaCommand extends AbstractCommandExecutor {

    private final AbstractCommandExecutor parentCommandExecutor;
    private final Cache<UUID, Long> timeout = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.SECONDS)
        .build();

    public TpaCommand(List<ICommand> commands, AbstractCommandExecutor parentCommandExecutor) {
        super("tpa", commands);
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
                if (this.timeout.getIfPresent(((Player) sender).getUniqueId()) != null) {
                    sender.sendMessage(Messages.TPA_TIMEOUT.getMessage());
                    return false;
                }
                this.timeout.put(((Player) sender).getUniqueId(), System.currentTimeMillis());

                final Player target = Bukkit.getPlayer(subCommandName);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
                    return false;
                }

                target.setMetadata("tpa", new FixedMetadataValue(Factions.getPlugin(), sender.getName()));
                return true;
            }
        }

        return this.parentCommandExecutor.onCommand(sender, command, label, args);
    }
}
