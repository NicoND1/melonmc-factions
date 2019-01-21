package de.melonmc.factions;
import de.melonmc.factions.command.AbstractCommandExecutor;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.database.DatabaseSaver;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * @author Nico_ND1
 */
public interface Factions {

    DatabaseSaver getDatabaseSaver();

    AbstractCommandExecutor createCommandExecutor(List<ICommand> commands);

    static Factions getInstance() {
        return FactionsInstanceHolder.getFactions();
    }

    static Plugin getPlugin() {
        return FactionsInstanceHolder.getPlugin();
    }

}
