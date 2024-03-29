package de.melonmc.bukkit.manager.scoreboard;

import de.MelonMC.RufixHD.SystemAPI.APIs.ScoreboardAPI;
import de.melonmc.factions.Factions;
import de.melonmc.factions.player.FactionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created on 27.04.2019
 *
 * @author RufixHD
 */

public class ScorebaordManager {
    public static void setBoard(Player player) {
        String uuid = player.getUniqueId().toString();
        ScoreboardAPI.animationTitle = new String[]{
            "§8» §2F§araktionen",
            "§8» §aF§2r§aaktionen",
            "§8» §aFr§2a§aktionen",
            "§8» §aFra§2k§ationen",
            "§8» §aFrak§2t§aionen",
            "§8» §aFrakt§2i§aonen",
            "§8» §aFrakti§2o§anen",
            "§8» §aFraktio§2n§aen",
            "§8» §aFraktion§2e§an",
            "§8» §aFraktio§2n§aen",
            "§8» §aFrakti§2o§anen",
            "§8» §aFrakt§2i§aonen",
            "§8» §aFrak§2t§aionen",
            "§8» §aFra§2k§ationen",
            "§8» §aFr§2a§aktionen",
            "§8» §aF§2r§aaktionen",
            "§8» §2F§araktionen"};
        ScoreboardAPI.createBoard(player);
        ScoreboardAPI.setLineStetic(7, " ", player);
        ScoreboardAPI.setLineStetic(6, "× §7Fraktion", player);

        Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, player.getName(), null), optionalFactionsPlayer -> {
            final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
            Factions.getInstance().getDatabaseSaver().findFaction(factionsPlayer, optionalFaction -> {

                if (optionalFaction.isPresent()) {
                    ScoreboardAPI.setLine(5, " §8➥ §a" + optionalFaction.get().getName(), player);
                } else {
                    ScoreboardAPI.setLine(5, " §8➥ §aKeine", player);
                }

                ScoreboardAPI.setLineStetic(4, "  ", player);
                ScoreboardAPI.setLineStetic(3, "× §7Münzen", player);

                if (optionalFactionsPlayer.isPresent()) {
                    ScoreboardAPI.setLine(2, " §8➥ §a" +  optionalFactionsPlayer.get().getCoins(), player);
                } else {
                    ScoreboardAPI.setLine(2, " §8➥ §a0", player);
                }

                ScoreboardAPI.setLineStetic(1, "   ", player);

                for (Player all : Bukkit.getOnlinePlayers()) {
                    ScoreboardAPI.setPexPrefix(all);
                }

                player.setScoreboard(ScoreboardAPI.getBoard(player));
            });
        });
    }

    public static void updateBoard(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Factions.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    Factions.getInstance().getDatabaseSaver().findPlayer(new FactionsPlayer(null, all.getName(), null), optionalFactionsPlayer -> {
                        final FactionsPlayer factionsPlayer = optionalFactionsPlayer.get();
                        Factions.getInstance().getDatabaseSaver().findFaction(factionsPlayer, optionalFaction -> {

                            if (optionalFaction.isPresent()) {
                                all.setPlayerListName(all.getDisplayName() + " §8» §a§o" + optionalFaction.get().getTag());
                                ScoreboardAPI.updateBoard(5, " §8➥ §a" + optionalFaction.get().getName(), all.getScoreboard());
                            } else {
                                all.setPlayerListName(all.getDisplayName());
                                ScoreboardAPI.updateBoard(5, " §8➥ §aKeine", all.getScoreboard());
                            }

                            if (optionalFactionsPlayer.isPresent()) {
                                ScoreboardAPI.updateBoard(2, " §8➥ §a" +  optionalFactionsPlayer.get().getCoins(), all.getScoreboard());
                            } else {
                                ScoreboardAPI.updateBoard(2, " §8➥ §a0", all.getScoreboard());
                            }
                        });
                    });
                }
            }
        }, 0, 40);
    }
}
