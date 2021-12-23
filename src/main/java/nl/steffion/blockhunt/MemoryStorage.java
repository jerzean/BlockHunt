package nl.steffion.blockhunt;

/**
 * Steffion's Engine - Made by Steffion.
 * <p>
 * You're allowed to use this engine for own usage, you're not allowed to
 * republish the engine. Using this for your own plugin is allowed when a
 * credit is placed somewhere in the plugin.
 * <p>
 * Thanks for your cooperate!
 *
 * @author Steffion
 */

import nl.steffion.blockhunt.Managers.CommandManager;
import nl.steffion.blockhunt.Managers.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MemoryStorage {

    /*
     * Standard stuff.
     */
    public static ArrayList<String> newFiles = new ArrayList<>();
    public static ArrayList<CommandManager> commands = new ArrayList<>();

    /*
     * If you want another file to be created. Copy and paste this line.
     */
    public static ConfigManager config = new ConfigManager("config");
    public static ConfigManager messages = new ConfigManager("messages");
    public static ConfigManager arenas = new ConfigManager("arenas");
    public static ConfigManager signs = new ConfigManager("signs");
    public static ConfigManager shop = new ConfigManager("shop");

    /*
     * Add any variable you need in different classes here:
     */

    public static HashMap<Player, Location> pos1 = new HashMap<>();
    public static HashMap<Player, Location> pos2 = new HashMap<>();

    public static ArrayList<Arena> arenaList = new ArrayList<>();
    public static Random random = new Random();
    public static HashMap<Player, Integer> seekertime = new HashMap<>();

    public static HashMap<Player, PlayerArenaData> pData = new HashMap<>();
    public static HashMap<Player, ItemStack> choosenBlock = new HashMap<>();
    public static HashMap<Player, Boolean> choosenSeeker = new HashMap<>();

    public static HashMap<Player, Long> lastMove = new HashMap<>();

    public static HashMap<Player, Long> lastTauntUsed = new HashMap<>();

    public static HashMap<Player, ItemStack> pBlock = new HashMap<>();
    //Hidden Location
    public static HashMap<Player, Location> hiddenLoc = new HashMap<>();
    //If Hidden Location is a water bloc or not
    public static HashMap<Player, Boolean> hiddenLocWater = new HashMap<>();

    public static Map<Player, Location> teleportLoc = new HashMap<>();
}
