package de.melonmc.bukkit.command.npc;
import de.melonmc.factions.Factions;
import de.melonmc.factions.command.ICommand;
import de.melonmc.factions.database.NpcInformation;
import de.melonmc.factions.util.ConfigurableLocation;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Nico_ND1
 */
public class NpcSetCommand implements ICommand<Player> {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Result onExecute(Player sender, String label, String[] args) {
        if (args.length == 2) {
            final String nameHeader = args[0].replace("&", "§");
            final String nameFooter = args[1].replace("&", "§");

            Factions.getInstance().getDatabaseSaver().loadDefaultConfigurations(defaultConfigurations -> {

                sender.sendMessage(nameHeader);
                sender.sendMessage(nameFooter);

                if(nameHeader.equals("teleport")){
                    final Optional<NpcInformation> optionalNpcInformation = defaultConfigurations.getNpcInformations().stream()
                        .filter(npcInformation -> npcInformation.getNameFooter().equals(nameFooter))
                        .findAny();

                    if (optionalNpcInformation.isPresent()) {
                        optionalNpcInformation.ifPresent(npcInformation -> {
                            npcInformation.setTeleportLocation(new ConfigurableLocation(sender.getLocation()));
                            Factions.getInstance().getDatabaseSaver().saveDefaultConfigurations(defaultConfigurations, () -> sender.sendMessage("Die teleport Location wurde geupdatet."));
                        });
                    } else {
                        Factions.getInstance().getDatabaseSaver().saveDefaultConfigurations(defaultConfigurations, () -> sender.sendMessage("Setze zuerst den NPC spawn."));
                    }
                } else {
                    final Optional<NpcInformation> optionalNpcInformation = defaultConfigurations.getNpcInformations().stream()
                        .filter(npcInformation -> npcInformation.getNameHeader().equals(nameHeader))
                        .findAny();

                    if (optionalNpcInformation.isPresent()) {
                        optionalNpcInformation.ifPresent(npcInformation -> {
                            npcInformation.setLocation(new ConfigurableLocation(sender.getLocation()));
                            Factions.getInstance().getDatabaseSaver().saveDefaultConfigurations(defaultConfigurations, () -> sender.sendMessage("Die NPC spawn Location wurde geupdatet."));
                        });
                    } else {
                        defaultConfigurations.getNpcInformations().add(new NpcInformation(
                            nameHeader,
                            nameFooter,
                            new ConfigurableLocation(sender.getLocation()),
                            new ConfigurableLocation("world", 0, 0, 0, 0, 0)
                        ));
                        Factions.getInstance().getDatabaseSaver().saveDefaultConfigurations(defaultConfigurations, () -> sender.sendMessage("NPC Hinzugefügt."));
                    }
                }
            });
            return Result.SUCCESSFUL;
        }

        return Result.WRONG_ARGUMENTS;
    }
}
