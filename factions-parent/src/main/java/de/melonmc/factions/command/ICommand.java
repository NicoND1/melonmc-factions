package de.melonmc.factions.command;
import org.bukkit.command.CommandSender;

/**
 * @author Nico_ND1
 */
public interface ICommand<T extends CommandSender> {

    String getName(); // Includes command usage: example <LALA> [LALA]

    String[] getAliases();

    Result onExecute(T sender, String label, String[] args);

    public enum Result {

        SUCCESSFUL,
        WRONG_ARGUMENTS,
        OTHER;

    }

}
