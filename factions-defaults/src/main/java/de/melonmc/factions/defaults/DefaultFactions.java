package de.melonmc.factions.defaults;
import de.melonmc.factions.Factions;
import de.melonmc.factions.chunk.ChunkManager;
import de.melonmc.factions.command.AbstractCommandExecutor;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.database.DatabaseSaver;
import de.melonmc.factions.defaults.command.DefaultCommandExecutor;
import de.melonmc.factions.invitation.InvitationManager;
import de.melonmc.factions.job.JobManager;
import de.melonmc.factions.trade.TradeManager;
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
    private final ChunkManager chunkManager;
    private final JobManager jobManager;
    private final InvitationManager invitationManager;
    private final TradeManager tradeManager;

    @Override
    public AbstractCommandExecutor createCommandExecutor(String commandName, List<ICommand> commands) {
        return new DefaultCommandExecutor(commandName, commands);
    }
}
