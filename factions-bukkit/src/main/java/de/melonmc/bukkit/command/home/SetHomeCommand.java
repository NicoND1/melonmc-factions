package de.melonmc.bukkit.command.home;
import de.melonmc.factions.Factions;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.entity.Player;

/**
 * @author Nico_ND1
 */
public class SetHomeCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player player, String label, String[] args) {
        Factions.getInstance().getDatabaseSaver().findHomes(new FactionsPlayer(player), homes -> {
            final int maxHomes = FactionsPlayer.HOMES_PER_PLAYER;

            if(homes.size() >= maxHomes) {

                return;
            }
        });
        return null;
    }
}
