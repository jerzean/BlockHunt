package nl.steffion.blockhunt;

import nl.steffion.blockhunt.Managers.MessageManager;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;

public class SignsHandler {

    public static void createSign(SignChangeEvent event, String[] lines, Location location) {
        if (lines[1] != null) {
            if (lines[1].equalsIgnoreCase("leave")) {
                boolean saved = false;
                int number = 1;
                while (!saved) {
                    if (MemoryStorage.signs.getFile().get("leave_" + number) == null) {
                        MemoryStorage.signs.getFile().set("leave_" + number + ".arenaName", "leave");
                        MemoryStorage.signs.getFile().set("leave_" + number + ".location", location);
                        MemoryStorage.signs.save();

                        saved = true;
                    } else {
                        number = number + 1;
                    }
                }
            } else if (lines[1].equalsIgnoreCase("shop")) {
                boolean saved = false;
                int number = 1;
                while (!saved) {
                    if (MemoryStorage.signs.getFile().get("shop_" + number) == null) {
                        MemoryStorage.signs.getFile().set("shop_" + number + ".arenaName", "shop");
                        MemoryStorage.signs.getFile().set("shop_" + number + ".location", location);
                        MemoryStorage.signs.save();

                        saved = true;
                    } else {
                        number = number + 1;
                    }
                }
            } else {
                boolean saved = false;
                for (Arena arena : MemoryStorage.arenaList) {
                    if (lines[1].equals(arena.arenaName)) {
                        int number = 1;
                        while (!saved) {
                            if (MemoryStorage.signs.getFile().get(arena.arenaName + "_" + number) == null) {
                                MemoryStorage.signs.getFile().set(arena.arenaName + "_" + number + ".arenaName", lines[1]);
                                MemoryStorage.signs.getFile().set(arena.arenaName + "_" + number + ".location", location);
                                MemoryStorage.signs.save();

                                saved = true;
                            } else {
                                number = number + 1;
                            }
                        }
                    }
                }

                if (!saved) {
                    MessageManager.sendFMessage(event.getPlayer(), ConfigC.error_noArena, "name-" + lines[1]);
                }
            }
        }
    }

    public static void removeSign(Location location) {
        for (String sign : MemoryStorage.signs.getFile().getKeys(false)) {
            Location loc = (Location) MemoryStorage.signs.getFile().get(sign + ".location");
            if (loc.equals(location)) {
                MemoryStorage.signs.getFile().set(sign, null);
                MemoryStorage.signs.save();
            }
        }
    }

    public static boolean isSign(Location location) {
        for (String sign : MemoryStorage.signs.getFile().getKeys(false)) {
            Location loc = (Location) MemoryStorage.signs.getFile().get(sign + ".location");
            if (loc.equals(location)) {
                return true;
            }
        }

        return false;
    }

    public static void updateSigns() {
        MemoryStorage.signs.load();
        for (String sign : MemoryStorage.signs.getFile().getKeys(false)) {
            Location loc = (Location) MemoryStorage.signs.getFile().get(sign + ".location");

            // check if that area is actually loaded. If not move on.
            if (loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
                if (loc.getBlock().getState() instanceof Sign) {
                    Sign signblock = (Sign) loc.getBlock().getState();
                    String[] lines = signblock.getLines();
                    if (sign.contains("leave")) {

                        ArrayList<String> signLines = (ArrayList<String>) MemoryStorage.config.getFile().getStringList(ConfigC.sign_LEAVE.location);
                        int linecount = 0;
                        for (String line : signLines) {
                            if (linecount <= 3) {
                                signblock.setLine(linecount, MessageManager.replaceAll(line));
                            }

                            linecount = linecount + 1;
                        }
                        signblock.update();
                    } else if (sign.contains("shop")) {
                        ArrayList<String> signLines = (ArrayList<String>) MemoryStorage.config.getFile().getStringList(ConfigC.sign_SHOP.location);
                        int linecount = 0;
                        for (String line : signLines) {
                            if (linecount <= 3) {
                                signblock.setLine(linecount, MessageManager.replaceAll(line));
                            }

                            linecount = linecount + 1;
                        }
                        signblock.update();
                    } else {
                        for (Arena arena : MemoryStorage.arenaList) {
                            if (lines[1].endsWith(arena.arenaName)) {
                                if (arena.gameState.equals(Arena.ArenaState.WAITING)) {
                                    ArrayList<String> signLines = (ArrayList<String>) MemoryStorage.config.getFile().getStringList(ConfigC.sign_WAITING.location);
                                    int linecount = 0;
                                    for (String line : signLines) {
                                        if (linecount <= 3) {
                                            signblock.setLine(
                                                    linecount,
                                                    MessageManager.replaceAll(line, "arenaname-" + arena.arenaName, "players-" + arena.playersInArena.size(), "maxplayers-"
                                                            + arena.maxPlayers, "timeleft-" + arena.timer));
                                        }

                                        linecount = linecount + 1;
                                    }
                                    signblock.update();
                                } else if (arena.gameState.equals(Arena.ArenaState.STARTING)) {
                                    ArrayList<String> signLines = (ArrayList<String>) MemoryStorage.config.getFile().getStringList(ConfigC.sign_STARTING.location);
                                    int linecount = 0;
                                    for (String line : signLines) {
                                        if (linecount <= 3) {
                                            signblock.setLine(
                                                    linecount,
                                                    MessageManager.replaceAll(line, "arenaname-" + arena.arenaName, "players-" + arena.playersInArena.size(), "maxplayers-"
                                                            + arena.maxPlayers, "timeleft-" + arena.timer));
                                        }

                                        linecount = linecount + 1;
                                    }
                                    signblock.update();
                                } else if (arena.gameState.equals(Arena.ArenaState.STARTED)) {
                                    ArrayList<String> signLines = (ArrayList<String>) MemoryStorage.config.getFile().getStringList(ConfigC.sign_INGAME.location);
                                    int linecount = 0;
                                    for (String line : signLines) {
                                        if (linecount <= 3) {
                                            signblock.setLine(
                                                    linecount,
                                                    MessageManager.replaceAll(line, "arenaname-" + arena.arenaName, "players-" + arena.playersInArena.size(), "maxplayers-"
                                                            + arena.maxPlayers, "timeleft-" + arena.timer));
                                        }

                                        linecount = linecount + 1;
                                    }
                                    signblock.update();
                                }
                            }
                        }
                    }
                } else {
                    removeSign(loc);
                }
            }
        }
    }
}
