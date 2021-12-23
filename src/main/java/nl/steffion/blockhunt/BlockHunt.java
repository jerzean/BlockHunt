package nl.steffion.blockhunt;

import nl.steffion.blockhunt.Commands.*;
import nl.steffion.blockhunt.Listeners.*;
import nl.steffion.blockhunt.Managers.*;
import nl.steffion.blockhunt.Taunt.EntityBalloon;
import nl.steffion.blockhunt.Taunt.SimpleSoundTaunt;
import nl.steffion.blockhunt.Taunt.SplashPotionTaunt;
import nl.steffion.blockhunt.Taunt.Taunt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockHunt extends JavaPlugin implements Listener {
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

    public static PluginDescriptionFile pdfFile;
    public static BlockHunt plugin;

    public static final List<String> blockHuntMainSuggestion = Arrays.asList("info", "help", "reload", "join", "leave", "list", "shop",
            "start", "wand", "create", "set", "setwarp", "remove", "tokens");

    public static final List<String> blockHuntWarpChoice = Arrays.asList("lobby", "hiders", "seekers", "spawn");


    public static CommandManager CMD;
    public static CommandManager CMDinfo;
    public static CommandManager CMDhelp;
    public static CommandManager CMDreload;
    public static CommandManager CMDjoin;
    public static CommandManager CMDleave;
    public static CommandManager CMDlist;
    public static CommandManager CMDshop;
    public static CommandManager CMDstart;
    public static CommandManager CMDwand;
    public static CommandManager CMDcreate;
    public static CommandManager CMDset;
    public static CommandManager CMDsetwarp;
    public static CommandManager CMDremove;
    public static CommandManager CMDtokens;

    /**
     * Args to String. Makes 1 string.
     *
     * @param input    String list which should be converted to a string.
     * @param startArg Start on this length.
     * @return The converted string.
     */
    public static String stringBuilder(String[] input, int startArg) {
        if (input.length - startArg <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(input[startArg]);
        for (int i = ++startArg; i < input.length; i++) {
            sb.append(' ').append(input[i]);
        }
        return sb.toString();
    }

    /**
     * Short a String for like the Scoreboard title.
     *
     * @param string    String to be shorten.
     * @param maxLenght Max lenght of the characters.
     * @return Shorten string, else normal string.
     */
    public static String cutString(String string, int maxLenght) {
        if (string.length() > maxLenght) {
            string = string.substring(0, maxLenght);
        }
        return string;
    }

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getServer().getPluginManager().registerEvents(new BlockEvent(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new OnFoodLevelChangeEvent(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryCloseEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerSwapHandItemsEvent(), this);

        getServer().getPluginManager().registerEvents(new OnPlayerDropItemEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerTeleportEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new OnSignChangeEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerTauntEvent(), this);

        ConfigurationSerialization.registerClass(Arena.class, "BlockHuntArena");

        pdfFile = getDescription();
        plugin = this;

        ConfigManager.newFiles();

        loadCommand();

        if (!getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            MessageManager.broadcastFMessage(ConfigC.error_libsDisguisesNotInstalled);
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            MessageManager.broadcastFMessage(ConfigC.error_protocolLibNotInstalled);
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        TauntManager.register(Taunt.builder("creeper",
                ItemManager.generateStack(Material.CREEPER_SPAWN_EGG, 1, ChatColor.DARK_GREEN + "Psssst"))
                .setTauntSupplier(new SimpleSoundTaunt(Sound.ENTITY_CREEPER_PRIMED, 1.5F, 1))
                .setDelayTime(10)
                .build());

        TauntManager.register(Taunt.builder("sheep",
                ItemManager.generateStack(Material.SHEEP_SPAWN_EGG, 1, ChatColor.DARK_GREEN + "béaaaaaaaahhhh"))
                .setTauntSupplier(new SimpleSoundTaunt(Sound.ENTITY_SHEEP_AMBIENT, 1.5F, 1))
                .setDelayTime(10)
                .build());

        TauntManager.register(Taunt.builder("cow",
                ItemManager.generateStack(Material.COW_SPAWN_EGG, 1, ChatColor.DARK_GREEN + "meeeeeeeeow"))
                .setTauntSupplier(new SimpleSoundTaunt(Sound.ENTITY_COW_AMBIENT, 1.5F, 1))
                .setDelayTime(10)
                .build());

        TauntManager.register(Taunt.builder("enderman",
                ItemManager.generateStack(Material.ENDERMAN_SPAWN_EGG, 1, ChatColor.DARK_GREEN + "FCOIHSQDJBFU9UIHFDBPSQIJD"))
                .setTauntSupplier(new SimpleSoundTaunt(Sound.ENTITY_ENDERMAN_AMBIENT, 1.5F, 1))
                .setDelayTime(10)
                .build());

        TauntManager.register(Taunt.builder("sorcière",
                ItemManager.generateStack(Material.WITCH_SPAWN_EGG, 1, ChatColor.DARK_GREEN + "Witch"))
                .setTauntSupplier(new SimpleSoundTaunt(Sound.ENTITY_WITCH_AMBIENT, 1.5F, 1))
                .setDelayTime(10)
                .build());

        TauntManager.register(Taunt.builder("potion",
                ItemManager.generateStack(Material.SPLASH_POTION, 1, ChatColor.LIGHT_PURPLE + "Spalsh ! Drink !"))
                .setTauntSupplier(new SplashPotionTaunt())
                .setDelayTime(30)
                .build());

        TauntManager.register(Taunt.builder("bzzz",
                ItemManager.generateStack(Material.YELLOW_WOOL, 1, ChatColor.LIGHT_PURPLE + "Bzzzzzzz bzzzzzzz !"))
                .setTauntSupplier(new EntityBalloon(EntityType.SHEEP, ChatColor.YELLOW + " Bzzzz Bzzz i am a bee !!"))
                .setDelayTime(30)
                .build());

        TauntManager.register(Taunt.builder("creeper_boom",
                ItemManager.generateStack(Material.CREEPER_HEAD, 1, ChatColor.GREEN + "CreeperBomb"))
                .setTauntSupplier(new EntityBalloon(EntityType.CREEPER, ChatColor.GREEN + " WAKA BOOOOM !"))
                .setDelayTime(30)
                .build());

        MessageManager.sendFMessage(null, ConfigC.log_enabledPlugin, "name-" + BlockHunt.pdfFile.getName(), "version-" + BlockHunt.pdfFile.getVersion(), "authors-" + BlockHunt.pdfFile.getAuthors().get(0));

        getServer().getScheduler().runTaskLater(this, ArenaHandler::loadArenas, 10);

        // Welcome to the massive game loop!!
        new GameLoopTask().runTaskTimer(this, 20, 20);
    }

    public void onDisable() {
        for (Arena arena : MemoryStorage.arenaList) {
            String cause = "[BlockHunt] Arena " + arena.arenaName + " has been stopped";
            ArenaHandler.stopArena(arena, cause, ConfigC.warning_arenaStopped);
        }

        MessageManager.sendFMessage(null, ConfigC.log_disabledPlugin, "name-" + BlockHunt.pdfFile.getName(), "version-" + BlockHunt.pdfFile.getVersion(), "authors-"
                + BlockHunt.pdfFile.getAuthors().get(0));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        for (CommandManager command : MemoryStorage.commands) {
            String[] argsSplit = null;
            String[] argsSplitAlias = null;

            if (command.args != null && command.argsalias != null) {
                argsSplit = command.args.split("/");
                argsSplitAlias = command.argsalias.split("/");
            }

            if (cmd.getName().equalsIgnoreCase(command.label)) {
                boolean equals = true;

                if (argsSplit == null) {
                    equals = args.length == 0;
                } else {
                    if (args.length >= argsSplit.length) {
                        for (int i2 = argsSplit.length - 1; i2 >= 0; i2 = i2 - 1) {
                            int loc = argsSplit.length - i2 - 1;
                            if (!argsSplit[loc].equalsIgnoreCase(args[loc]) && !argsSplitAlias[loc].equalsIgnoreCase(args[loc])) {
                                equals = false;
                            }
                        }
                    } else {
                        equals = false;
                    }
                }

                if (equals) {
                    if (PermissionsManager.hasPerm(player, command.permission, true)) {
                        if (command.enabled) {
                            command.CMD.execute(player, cmd, label, args);
                        } else {
                            MessageManager.sendFMessage(player, ConfigC.error_commandNotEnabled);
                        }
                    }

                    return true;
                }
            }
        }
        new CMDnotfound().execute(player, cmd, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("blockhunt")) {
            if (args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], blockHuntMainSuggestion, new ArrayList<>());
            } else if (args.length == 2) {
                String entry = args[0];
                if (entry.equalsIgnoreCase("join") || entry.equalsIgnoreCase("j")
                        || entry.equalsIgnoreCase("start") || entry.equalsIgnoreCase("go")
                        || entry.equalsIgnoreCase("set") || entry.equalsIgnoreCase("s")
                        || entry.equalsIgnoreCase("remove") || entry.equalsIgnoreCase("delete")) {
                    return StringUtil.copyPartialMatches(args[1], getArenaNameList(), new ArrayList<>());
                } else if (entry.equalsIgnoreCase("setwarp") || entry.equalsIgnoreCase("sw")) {
                    return StringUtil.copyPartialMatches(args[1], blockHuntWarpChoice, new ArrayList<>());
                }
            } else if (args.length == 3) {
                String entry = args[1];
                if (blockHuntWarpChoice.contains(entry.toLowerCase())) {
                    return StringUtil.copyPartialMatches(args[2], getArenaNameList(), new ArrayList<>());
                }
            }
        }

        return null;
    }

    public List<String> getArenaNameList() {
        return MemoryStorage.arenaList.stream().map(arena -> arena.arenaName).collect(Collectors.toList());
    }

    public void loadCommand() {
        CMD = new CommandManager("BlockHunt", "BlockHunt", null, null, PermissionsC.Permissions.info, ConfigC.help_info, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_info),
                new CMDinfo(), null);
        CMDinfo = new CommandManager("BlockHunt INFO", "BlockHunt", "info", "i", PermissionsC.Permissions.info, ConfigC.help_info, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_info), new CMDinfo(), "/BlockHunt [info|i]");
        CMDhelp = new CommandManager("BlockHunt HELP", "BlockHunt", "help", "h", PermissionsC.Permissions.help, ConfigC.help_help, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_help), new CMDhelp(), "/BlockHunt <help|h> [page number]");
        CMDreload = new CommandManager("BlockHunt RELOAD", "BlockHunt", "reload", "r", PermissionsC.Permissions.reload, ConfigC.help_reload,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_reload), new CMDreload(), "/BlockHunt <reload|r>");
        CMDjoin = new CommandManager("BlockHunt JOIN", "BlockHunt", "join", "j", PermissionsC.Permissions.join, ConfigC.help_join, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_join), new CMDjoin(), "/BlockHunt <join|j> <arenaname>");
        CMDleave = new CommandManager("BlockHunt LEAVE", "BlockHunt", "leave", "l", PermissionsC.Permissions.leave, ConfigC.help_leave,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_leave), new CMDleave(), "/BlockHunt <leave|l>");
        CMDlist = new CommandManager("BlockHunt LIST", "BlockHunt", "list", "li", PermissionsC.Permissions.list, ConfigC.help_list, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_list), new CMDlist(), "/BlockHunt <list|li>");
        CMDshop = new CommandManager("BlockHunt SHOP", "BlockHunt", "shop", "sh", PermissionsC.Permissions.shop, ConfigC.help_shop, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_shop), new CMDshop(), "/BlockHunt <shop|sh>");
        CMDstart = new CommandManager("BlockHunt START", "BlockHunt", "start", "go", PermissionsC.Permissions.start, ConfigC.help_start,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_start), new CMDstart(), "/BlockHunt <start|go> <arenaname>");
        CMDwand = new CommandManager("BlockHunt WAND", "BlockHunt", "wand", "w", PermissionsC.Permissions.create, ConfigC.help_wand, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_wand), new CMDwand(), "/BlockHunt <wand|w>");
        CMDcreate = new CommandManager("BlockHunt CREATE", "BlockHunt", "create", "c", PermissionsC.Permissions.create, ConfigC.help_create,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_create), new CMDcreate(), "/BlockHunt <create|c> <arenaname>");
        CMDset = new CommandManager("BlockHunt SET", "BlockHunt", "set", "s", PermissionsC.Permissions.set, ConfigC.help_set, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_set), new CMDset(), "/BlockHunt <set|s> <arenaname>");
        CMDsetwarp = new CommandManager("BlockHunt SETWARP", "BlockHunt", "setwarp", "sw", PermissionsC.Permissions.setwarp, ConfigC.help_setwarp,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_setwarp), new CMDsetwarp(),
                "/BlockHunt <setwarp|sw> <lobby|hiders|seekers|spawn> <arenaname>");
        CMDremove = new CommandManager("BlockHunt REMOVE", "BlockHunt", "remove", "delete", PermissionsC.Permissions.remove, ConfigC.help_remove,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_remove), new CMDremove(), "/BlockHunt <remove|delete> <arenaname>");
        CMDtokens = new CommandManager("BlockHunt TOKENS", "BlockHunt", "tokens", "t", PermissionsC.Permissions.tokens, ConfigC.help_tokens,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_tokens), new CMDtokens(), "/BlockHunt <tokens|t> <set|add|take> <playername> <amount>");

    }
}
