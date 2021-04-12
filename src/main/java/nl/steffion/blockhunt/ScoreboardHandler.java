package nl.steffion.blockhunt;

import nl.steffion.blockhunt.Managers.TauntManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.IntStream;

public class ScoreboardHandler {

    private static final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
    private static final SimpleDateFormat msFormater = new SimpleDateFormat("ss");
    private static final String gameState = "DummySate";

    public static void updateScoreboard(Arena arena) {
        if (arena.playersInArena.size() > 0 && scoreboardManager != null) {
            for (Player player : arena.playersInArena) {
                Scoreboard scoreboard = arena.scoreboardPlayerMap.computeIfAbsent(player, pl -> scoreboardManager.getNewScoreboard());

                if (!player.getScoreboard().equals(scoreboard)) {
                    player.setScoreboard(scoreboard);
                }

                Objective mainObjective = scoreboard.getObjective(arena.arenaName);
                Objective dummyState = scoreboard.getObjective(gameState);

                if (mainObjective == null || dummyState == null) {
                    mainObjective = scoreboard.registerNewObjective(arena.arenaName, "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + arena.arenaName);
                    dummyState = scoreboard.registerNewObjective(gameState, "dummy", "Bite");
                    fillMainObjective(mainObjective, 8);
                    mainObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
                }

                if (arena.gameState == Arena.ArenaState.WAITING) {
                    int oldState = getScore(dummyState, gameState);
                    if (oldState != 1) {
                        setScore(dummyState, gameState, 1);
                        clearAllScore(scoreboard, mainObjective);
                        fillMainObjective(mainObjective, 8);
                        updateScore(mainObjective, ChatColor.AQUA + "" + ChatColor.BOLD + "Block Selected: ", 3);
                    }

                    int currentPlayer = arena.playersInArena.size();
                    updateScore(mainObjective,
                            ChatColor.AQUA + "Players: " + ChatColor.YELLOW + currentPlayer + ChatColor.GRAY + "/" + ChatColor.YELLOW + arena.maxPlayers,
                            7);

                    int waitingPlayers = arena.minPlayers - currentPlayer;
                    updateScore(mainObjective, ChatColor.GOLD + "Waiting " + ChatColor.LIGHT_PURPLE + waitingPlayers + ChatColor.GOLD + " Players", 5);

                    ItemStack selectedStack = MemoryStorage.choosenBlock.get(player);
                    String selected = selectedStack == null ? "Nothing" : WordUtils.capitalizeFully(selectedStack.getType().name().toLowerCase().replace("_", " "));
                    updateScore(mainObjective, ChatColor.YELLOW + selected, 2);
                } else if (arena.gameState == Arena.ArenaState.STARTING) {
                    int oldState = getScore(dummyState, gameState);
                    if (oldState != 2) {
                        setScore(dummyState, gameState, 2);
                        clearAllScore(scoreboard, mainObjective);
                        fillMainObjective(mainObjective, 9);
                        updateScore(mainObjective, ChatColor.BLUE + "" + ChatColor.BOLD + "Starting in: ", 8);
                        updateScore(mainObjective, ChatColor.AQUA + "" + ChatColor.BOLD + "Block Selected: ", 3);
                    }

                    long timeToStart = arena.timer * 1000L;
                    updateScore(mainObjective, ChatColor.YELLOW + formatter.format(new Date(timeToStart)), 7);


                    int currentPlayer = arena.playersInArena.size();
                    updateScore(mainObjective,
                            ChatColor.AQUA + "Players: " + ChatColor.YELLOW + currentPlayer + ChatColor.GRAY + "/" + ChatColor.YELLOW + arena.maxPlayers,
                            5);

                    ItemStack selectedStack = MemoryStorage.choosenBlock.get(player);
                    String selected = selectedStack == null ? "Nothing" : WordUtils.capitalizeFully(selectedStack.getType().name().toLowerCase().replace("_", " "));
                    updateScore(mainObjective, ChatColor.YELLOW + selected, 2);
                } else if (arena.gameState == Arena.ArenaState.STARTED) {
                    int oldState = getScore(dummyState, gameState);
                    if (oldState != 3) {
                        setScore(dummyState, gameState, 3);
                        clearAllScore(scoreboard, mainObjective);
                        fillMainObjective(mainObjective, 10);
                        updateScore(mainObjective, ChatColor.BLUE + "" + ChatColor.BOLD + "Time Left:", 9);
                        updateScore(mainObjective, ChatColor.AQUA + "" + ChatColor.BOLD + "Players Alive: ", 6);
                    }

                    long time = arena.timer * 1000L;
                    updateScore(mainObjective, ChatColor.YELLOW + formatter.format(new Date(time)), 8);

                    int hiders = Math.toIntExact(arena.playersInArena.stream().filter(pl -> !arena.seekers.contains(pl)).count());
                    int seekers = arena.seekers.size();

                    String hidersMessage = ChatColor.YELLOW + "" + hiders + ChatColor.GREEN + " Hiders";
                    String seekersMessage = ChatColor.YELLOW + "" + seekers + ChatColor.GOLD + " Seekers";

                    updateScore(mainObjective, hidersMessage, 5);
                    updateScore(mainObjective, seekersMessage, 4);


                    if (!arena.seekers.contains(player)) {
                        boolean cantUseTaunt = TauntManager.canTaunt(player);
                        if (cantUseTaunt) {
                            updateScore(mainObjective, ChatColor.GREEN + "Taunt Available", 1);
                        } else {
                            long remainingTime = TauntManager.getRemainingTime(player);
                            updateScore(mainObjective, ChatColor.RED + "Taunt Cooldown (" + msFormater.format(new Date(remainingTime)) + ")", 1);
                        }
                    } else {
                        updateScore(mainObjective, ChatColor.RED + "", 1);
                    }
                }
            }
        }
    }

    private static String getFirstEntry(Scoreboard scoreboard, Objective objective, int score) {
        for (String entry : scoreboard.getEntries())
            if (objective.getScore(entry).isScoreSet() && objective.getScore(entry).getScore() == score)
                return entry;
        return null;
    }

    private static int getScore(Objective objective, String entry) {
        return objective.getScore(entry).isScoreSet() ? objective.getScore(entry).getScore() : -1;
    }

    private static void setScore(Objective objective, String entry, int score) {
        objective.getScore(BlockHunt.cutString(entry, 32)).setScore(score);
    }

    private static void fillMainObjective(Objective objective, int filled) {
        StringBuilder builder = new StringBuilder();
        IntStream.range(1, filled + 1).forEach(value -> objective.getScore(builder.append(ChatColor.RESET).toString()).setScore(value));
    }

    private static void clearScore(Scoreboard scoreboard, Objective objective, int score) {
        for (String entry : scoreboard.getEntries()) {
            if (objective.getScore(entry).isScoreSet() && objective.getScore(entry).getScore() == score)
                scoreboard.resetScores(entry);
        }
    }

    private static void clearAllScore(Scoreboard scoreboard, Objective objective) {
        for (String entry : scoreboard.getEntries()) {
            if (objective.getScore(entry).isScoreSet())
                scoreboard.resetScores(entry);
        }
    }

    private static void updateScore(Objective objective, String entry, int score) {
        Scoreboard scoreboard = objective.getScoreboard();
        if (scoreboard == null)
            return;
        String current = getFirstEntry(scoreboard, objective, score);
        boolean modify = current == null || !current.equals(entry);
        if (modify) {
            clearScore(scoreboard, objective, score);
            setScore(objective, entry, score);
        }
    }

    public static void removeScoreboard(Player player) {
        if (scoreboardManager != null)
            player.setScoreboard(scoreboardManager.getMainScoreboard());
    }
}
