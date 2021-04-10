package nl.Steffion.BlockHunt;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.FallingBlockWatcher;
import nl.Steffion.BlockHunt.Arena.ArenaState;
import nl.Steffion.BlockHunt.Commands.*;
import nl.Steffion.BlockHunt.Listeners.*;
import nl.Steffion.BlockHunt.Managers.CommandManager;
import nl.Steffion.BlockHunt.Managers.ConfigManager;
import nl.Steffion.BlockHunt.Managers.MessageManager;
import nl.Steffion.BlockHunt.Managers.PermissionsManager;
import nl.Steffion.BlockHunt.PermissionsC.Permissions;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

        getServer().getPluginManager().registerEvents(new OnBlockBreakEvent(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlaceEvent(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamageByEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new OnFoodLevelChangeEvent(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryCloseEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerSwapHandItemsEvent(), this);

        // Removed - This is handled by WorldGuard now.
        // getServer().getPluginManager().registerEvents(
        // new OnPlayerCommandPreprocessEvent(), this);

        getServer().getPluginManager().registerEvents(new OnPlayerDropItemEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerTeleportEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new OnSignChangeEvent(), this);

        ConfigurationSerialization.registerClass(Arena.class, "BlockHuntArena");

        pdfFile = getDescription();
        plugin = this;

        ConfigManager.newFiles();

        CMD = new CommandManager("BlockHunt", "BlockHunt", null, null, Permissions.info, ConfigC.help_info, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_info),
                new CMDinfo(), null);
        CMDinfo = new CommandManager("BlockHunt INFO", "BlockHunt", "info", "i", Permissions.info, ConfigC.help_info, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_info), new CMDinfo(), "/BlockHunt [info|i]");
        CMDhelp = new CommandManager("BlockHunt HELP", "BlockHunt", "help", "h", Permissions.help, ConfigC.help_help, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_help), new CMDhelp(), "/BlockHunt <help|h> [page number]");
        CMDreload = new CommandManager("BlockHunt RELOAD", "BlockHunt", "reload", "r", Permissions.reload, ConfigC.help_reload,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_reload), new CMDreload(), "/BlockHunt <reload|r>");
        CMDjoin = new CommandManager("BlockHunt JOIN", "BlockHunt", "join", "j", Permissions.join, ConfigC.help_join, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_join), new CMDjoin(), "/BlockHunt <join|j> <arenaname>");
        CMDleave = new CommandManager("BlockHunt LEAVE", "BlockHunt", "leave", "l", Permissions.leave, ConfigC.help_leave,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_leave), new CMDleave(), "/BlockHunt <leave|l>");
        CMDlist = new CommandManager("BlockHunt LIST", "BlockHunt", "list", "li", Permissions.list, ConfigC.help_list, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_list), new CMDlist(), "/BlockHunt <list|li>");
        CMDshop = new CommandManager("BlockHunt SHOP", "BlockHunt", "shop", "sh", Permissions.shop, ConfigC.help_shop, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_shop), new CMDshop(), "/BlockHunt <shop|sh>");
        CMDstart = new CommandManager("BlockHunt START", "BlockHunt", "start", "go", Permissions.start, ConfigC.help_start,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_start), new CMDstart(), "/BlockHunt <start|go> <arenaname>");
        CMDwand = new CommandManager("BlockHunt WAND", "BlockHunt", "wand", "w", Permissions.create, ConfigC.help_wand, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_wand), new CMDwand(), "/BlockHunt <wand|w>");
        CMDcreate = new CommandManager("BlockHunt CREATE", "BlockHunt", "create", "c", Permissions.create, ConfigC.help_create,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_create), new CMDcreate(), "/BlockHunt <create|c> <arenaname>");
        CMDset = new CommandManager("BlockHunt SET", "BlockHunt", "set", "s", Permissions.set, ConfigC.help_set, (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_set), new CMDset(), "/BlockHunt <set|s> <arenaname>");
        CMDsetwarp = new CommandManager("BlockHunt SETWARP", "BlockHunt", "setwarp", "sw", Permissions.setwarp, ConfigC.help_setwarp,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_setwarp), new CMDsetwarp(),
                "/BlockHunt <setwarp|sw> <lobby|hiders|seekers|spawn> <arenaname>");
        CMDremove = new CommandManager("BlockHunt REMOVE", "BlockHunt", "remove", "delete", Permissions.remove, ConfigC.help_remove,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_remove), new CMDremove(), "/BlockHunt <remove|delete> <arenaname>");
        CMDtokens = new CommandManager("BlockHunt TOKENS", "BlockHunt", "tokens", "t", Permissions.tokens, ConfigC.help_tokens,
                (Boolean) MemoryStorage.config.get(ConfigC.commandEnabled_tokens), new CMDtokens(), "/BlockHunt <tokens|t> <set|add|take> <playername> <amount>");

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

        ArenaHandler.loadArenas();

        MessageManager.sendFMessage(null, ConfigC.log_enabledPlugin, "name-" + BlockHunt.pdfFile.getName(), "version-" + BlockHunt.pdfFile.getVersion(), "authors-"
                + BlockHunt.pdfFile.getAuthors().get(0));

        // Welcome to the massive game loop!!
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Arena arena : MemoryStorage.arenaList) {
                if (arena.gameState == ArenaState.WAITING) {
                    if (arena.playersInArena.size() >= arena.minPlayers) {
                        arena.gameState = ArenaState.STARTING;
                        arena.timer = arena.timeInLobbyUntilStart;
                        ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-" + arena.timeInLobbyUntilStart);
                    }
                } else if (arena.gameState == ArenaState.STARTING) {
                    arena.timer = arena.timer - 1;
                    if (arena.timer > 0) {
                        if (arena.timer == 60) {
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-60");
                        } else if (arena.timer == 30) {
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-30");
                        } else if (arena.timer == 10) {
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-10");
                        } else if (arena.timer == 5) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-5");
                        } else if (arena.timer == 4) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-4");
                        } else if (arena.timer == 3) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-3");
                        } else if (arena.timer == 2) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-2");
                        } else if (arena.timer == 1) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.5F));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-1");
                        }
                    } else {
                        //Start the game
                        arena.gameState = ArenaState.INGAME;
                        arena.timer = arena.gameTime;
                        ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaStarted, "secs-" + arena.waitingTimeSeeker);

                        arena.playersInArena.forEach(player -> arena.team.addEntry(player.getName()));

                        //seekers init
                        for (int i = arena.amountSeekersOnStart; i > 0; i = i - 1) {

                            boolean loop = true;
                            Player seeker = arena.getSeeker(player -> !arena.lastSeekers.equals(player.getUniqueId()));

                            for (Player playerCheck : arena.playersInArena) {
                                if (MemoryStorage.choosenSeeker.get(playerCheck) != null) {
                                    if (MemoryStorage.choosenSeeker.get(playerCheck)) {
                                        seeker = playerCheck;
                                        MemoryStorage.choosenSeeker.remove(playerCheck);
                                    } else {
                                        if (seeker.equals(playerCheck)) {
                                            i = i + 1;
                                            loop = false;
                                        }
                                    }
                                }
                            }

                            if (loop) {
                                if (!arena.seekers.contains(seeker)) {
                                    ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameSeekerChoosen, "seeker-" + seeker.getName());
                                    arena.seekers.add(seeker);
                                    arena.lastSeekers = seeker.getUniqueId();
                                    PlayerHandler.teleport(seeker, arena.seekersWarp);
                                    seeker.getInventory().clear();
                                    seeker.updateInventory();
                                    seeker.setWalkSpeed(0.3F);
                                    MemoryStorage.seekertime.put(seeker, arena.waitingTimeSeeker);
                                } else {
                                    i = i + 1;
                                }
                            }

                        }

                        //Hiders init and tp
                        for (Player arenaPlayer : arena.playersInArena) {
                            //If player is not a seeker
                            if (!arena.seekers.contains(arenaPlayer)) {
                                arenaPlayer.getInventory().clear();
                                arenaPlayer.updateInventory();
                                ItemStack block = arena.disguiseBlocks.get(MemoryStorage.random.nextInt(arena.disguiseBlocks.size()));

                                if (MemoryStorage.choosenBlock.get(arenaPlayer) != null) {
                                    block = MemoryStorage.choosenBlock.get(arenaPlayer);
                                    MemoryStorage.choosenBlock.remove(arenaPlayer);
                                }

                                MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, block.getType());
                                ((FallingBlockWatcher) disguise.getWatcher()).setGridLocked(false);
                                disguise.getWatcher().setNoGravity(true);
                                DisguiseAPI.disguiseToPlayers(arenaPlayer, disguise, arena.playersInArena);

                                PlayerHandler.teleport(arenaPlayer, arena.hidersWarp);
                                ItemStack blockCount = new ItemStack(block.getType(), 5);
                                arenaPlayer.getInventory().setItem(8, blockCount);
                                arenaPlayer.getInventory().setHelmet(new ItemStack(block));
                                MemoryStorage.pBlock.put(arenaPlayer, block);
                                String blockName = block.getType().name();
                                blockName = WordUtils.capitalizeFully(blockName.replace("_", " "));
                                MessageManager.sendFMessage(arenaPlayer, ConfigC.normal_ingameBlock, "block-" + blockName);
                            }
                        }
                    }
                }

                //If game has been started
                if (arena.gameState == ArenaState.INGAME) {
                    arena.timer = arena.timer - 1;
                    //If game is running
                    if (arena.timer > 0) {
                        //Give hiders sword
                        if (arena.timer == arena.gameTime - arena.timeUntilHidersSword) {
                            ItemStack sword = new ItemStack(Material.WOODEN_SWORD, 1);
                            sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
                            ItemMeta meta = sword.getItemMeta();
                            meta.setUnbreakable(true);
                            sword.setItemMeta(meta);

                            for (Player arenaPlayer : arena.playersInArena) {
                                if (!arena.seekers.contains(arenaPlayer)) {
                                    arenaPlayer.getInventory().addItem(sword.clone());
                                    MessageManager.sendFMessage(arenaPlayer, ConfigC.normal_ingameGivenSword);
                                }
                            }
                        }

                        // blockAnnouncer code.
                        if ((arena.blockAnnouncerTime > 0) && (arena.timer == arena.blockAnnouncerTime)) {
                            ArrayList<Material> remainingBlocks = new ArrayList<>();
                            AtomicInteger index = new AtomicInteger();

                            for (Player arenaPlayer : arena.playersInArena) {
                                if (!arena.seekers.contains(arenaPlayer)) {
                                    Material block = arenaPlayer.getInventory().getItem(8).getType();
                                    if (!remainingBlocks.contains(block)) { //Don't print double up block names.
                                        remainingBlocks.add(block);
                                    }
                                }
                            }

                            Bukkit.getScheduler().runTaskTimer(this, bukkitTask -> {
                                if (arena.gameState != ArenaState.INGAME) {
                                    bukkitTask.cancel();
                                    return;
                                }

                                ArrayList<Material> newRemaningBlock = new ArrayList<>();

                                for (Player arenaPlayer : arena.playersInArena) {
                                    if (!arena.seekers.contains(arenaPlayer)) {
                                        Material block = arenaPlayer.getInventory().getItem(8).getType();
                                        if (!newRemaningBlock.contains(block)) { //Don't print double up block names.
                                            newRemaningBlock.add(block);
                                        }
                                    }
                                }

                                if (remainingBlocks.size() != newRemaningBlock.size()) {
                                    index.set(0);
                                    remainingBlocks.clear();
                                    remainingBlocks.addAll(newRemaningBlock);
                                }

                                if (index.incrementAndGet() >= remainingBlocks.size())
                                    index.set(0);

                                Material remaningMat = remainingBlocks.get(index.get());

                                int count = Math.toIntExact(arena.playersInArena.stream().filter(player -> !arena.seekers.contains(player) && player.getInventory().getItem(8).getType() == remaningMat).count());
                                ItemStack remaningBlock = new ItemStack(remaningMat, count);

                                arena.seekers.forEach(player -> player.getInventory().setItem(8, remaningBlock));

                            }, 1, 25);

                            StringBuilder builder = new StringBuilder(ChatColor.GREEN + "Remaining Block: ");

                            for (Material material : remainingBlocks)
                                builder.append(ChatColor.BLUE)
                                        .append(material.name().toLowerCase().replace("_", " "))
                                        .append(", ");
                            String msg = builder.toString();
                            msg = msg.substring(0, msg.length() - 3);
                            String finalMsg = msg;
                            arena.playersInArena.forEach(player -> player.sendMessage(finalMsg));
                        }

                        if (arena.timer == 190) {
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-190");
                        } else if (arena.timer == 60) {
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-60");
                        } else if (arena.timer == 30) {
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-30");
                        } else if (arena.timer == 10) {
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-10");
                        } else if (arena.timer == 5) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-5");
                        } else if (arena.timer == 4) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-4");
                        } else if (arena.timer == 3) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-3");
                        } else if (arena.timer == 2) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-2");
                        } else if (arena.timer == 1) {
                            arena.playersInArena.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0));
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameArenaEnd, "1-1");
                        }

                        //Teleport seekers and set Item
                        for (Player player : arena.seekers) {
                            if (player.getInventory().getItem(0) == null || player.getInventory().getItem(0).getType() != Material.DIAMOND_SWORD) {
                                player.getInventory().clear();
                                ItemStack i = new ItemStack(Material.DIAMOND_SWORD, 1);
                                i.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
                                ItemMeta meta = i.getItemMeta();
                                meta.setUnbreakable(true);
                                i.setItemMeta(meta);
                                player.getInventory().setItem(0, i);
                                player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
                                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
                                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
                                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
                                ItemStack infBow = new ItemStack(Material.BOW, 1);
                                ItemMeta bowMeta = infBow.getItemMeta();
                                bowMeta.setUnbreakable(true);
                                infBow.setItemMeta(bowMeta);
                                infBow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                                player.getInventory().setItem(1, infBow);
                                player.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
                            }


                            if (MemoryStorage.seekertime.get(player) != null) {
                                MemoryStorage.seekertime.put(player, MemoryStorage.seekertime.get(player) - 1);
                                if (MemoryStorage.seekertime.get(player) <= 0) {
                                    PlayerHandler.teleport(player, arena.hidersWarp);
                                    MemoryStorage.seekertime.remove(player);
                                    ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameSeekerSpawned, "playername-" + player.getName());
                                }
                            }
                        }

                        //Movement Player
                        for (Player player : arena.playersInArena) {
                            if (!arena.seekers.contains(player)) {
                                Location pLoc = player.getLocation();

                                if (player.getInventory().getItem(8) == null) {
                                    if (MemoryStorage.pBlock.get(player) != null) {
                                        player.getInventory().setItem(8, MemoryStorage.pBlock.get(player));
                                        player.updateInventory();
                                    }
                                }

                                ItemStack itemBlock = player.getInventory().getItem(8) == null ? MemoryStorage.pBlock.get(player) : player.getInventory().getItem(8);

                                Block pBlock = player.getLocation().getBlock();
                                BlockData pBlockData = pBlock.getBlockData();

                                if (!(pBlockData.getMaterial().isAir() || pBlockData.getMaterial() == Material.WATER))
                                    MessageManager.sendFMessage(player, ConfigC.warning_ingameNoSolidPlace);

                                //Hiders Want to disguise
                                if (itemBlock.getAmount() > 1) {
                                    long lastMove = System.currentTimeMillis() - MemoryStorage.lastMove.getOrDefault(player, System.currentTimeMillis());

                                    //Timer to disguise start
                                    if ((pBlockData.getMaterial().isAir() || pBlockData.getMaterial() == Material.WATER) && lastMove > 500)
                                        itemBlock.setAmount(itemBlock.getAmount() - 1);
                                    else itemBlock.setAmount(5); // Nop, moved or other reset the timer

                                } else if (!MemoryStorage.hiddenLoc.containsKey(player)) {
                                    //Disguise the player !
                                    if (pBlockData.getMaterial().isAir() || pBlockData.getMaterial() == Material.WATER) {
                                        if (pBlockData.getMaterial() == Material.WATER) {
                                            MemoryStorage.hiddenLocWater.put(player, true);
                                        } else {
                                            MemoryStorage.hiddenLocWater.put(player, false);
                                        }

                                        if (DisguiseAPI.isDisguised(player)) {
                                            DisguiseAPI.undisguiseToAll(player);

                                            for (Player pl : arena.playersInArena) {
                                                if (!pl.equals(player)) {
                                                    pl.hidePlayer(this, player);
                                                    pl.sendBlockChange(pBlock.getLocation(), itemBlock.getType().createBlockData());
                                                }
                                            }

                                            itemBlock.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                                            player.playSound(pLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                            MemoryStorage.hiddenLoc.put(player, player.getLocation());
                                            String blockName = itemBlock.getType().name();
                                            blockName = WordUtils.capitalizeFully(blockName.replace("_", " "));
                                            MessageManager.sendFMessage(player, ConfigC.normal_ingameNowSolid, "block-" + blockName);
                                            //player.sendBlockChange(pBlock.getLocation(), block.getType().createBlockData());
                                        }

                                        for (Player pl : arena.playersInArena) {
                                            if (!pl.equals(player)) {
                                                pl.hidePlayer(this, player);
                                                pl.sendBlockChange(pBlock.getLocation(), itemBlock.getType().createBlockData());
                                            }
                                        }
                                    } else {
                                        MessageManager.sendFMessage(player, ConfigC.warning_ingameNoSolidPlace);
                                    }
                                }
                            }
                        }
                    } else {
                        //Hiders Win !
                        ArenaHandler.hidersWin(arena);
                        return;
                    }

                    ScoreboardHandler.updateScoreboard(arena); // TODO Only do this when needed (player added/removed)
                }

                for (Player pl : arena.playersInArena) {
                    pl.setLevel(arena.timer);
                    if (arena.seekers.contains(pl)) {
                        pl.setGameMode(GameMode.SURVIVAL);
                    } else {
                        pl.setGameMode(GameMode.ADVENTURE);
                    }
                }
            }
            SignsHandler.updateSigns(); //TODO Only do this when needed (gamestate change or player count change)
        }, 0, 20);
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

    /*

     */
}
