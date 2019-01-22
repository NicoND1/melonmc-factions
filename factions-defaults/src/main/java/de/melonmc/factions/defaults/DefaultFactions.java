package de.melonmc.factions.defaults;
import de.melonmc.factions.Factions;
import de.melonmc.factions.command.AbstractCommandExecutor;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.database.DatabaseSaver;
import de.melonmc.factions.defaults.command.DefaultCommandExecutor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class DefaultFactions implements Factions {

    private final DatabaseSaver databaseSaver;

    @Override
    public AbstractCommandExecutor createCommandExecutor(List<ICommand> commands) {
        return new DefaultCommandExecutor(commands);
    }
}