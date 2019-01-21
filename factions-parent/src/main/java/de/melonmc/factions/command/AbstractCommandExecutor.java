package de.melonmc.factions.command;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.command.CommandExecutor;

import java.util.List;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public abstract class AbstractCommandExecutor implements CommandExecutor {

    protected final List<ICommand> commands;
}
