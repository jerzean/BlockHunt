package nl.steffion.blockhunt;

import me.libraryaddict.disguise.DisguiseAPI;
import nl.steffion.blockhunt.Arena.ArenaState;
import nl.steffion.blockhunt.Events.EndArenaEvent;
import nl.steffion.blockhunt.Events.JoinArenaEvent;
import nl.steffion.blockhunt.Events.LeaveArenaEvent;
import nl.steffion.blockhunt.Listeners.OnPlayerInteractEvent;
import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.Managers.PermissionsManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class ArenaHandler {
    public static void loadArenas() {
        MemoryStorage.arenaList.clear();
        for (String arenaName : MemoryStorage.arenas.getFile().getKeys(false)) {
            MemoryStorage.arenaList.add((Arena) MemoryStorage.arenas.getFile().get(arenaName));
        }
    }

    public static void sendMessage(Arena arena, String message, String... vars) {
        for (Player player : arena.playersInArena) {
            String pMessage = message.replaceAll("%player%", player.getDisplayName());
            player.sendMessage(MessageManager.replaceAll(pMessage, vars));
        }
    }

    public static void sendFMessage(Arena arena, ConfigC location, String... vars) {
        for (Player player : arena.playersInArena) {
            String pMessage = location.config.getFile().get(location.location).toString().replaceAll("%player%", player.getDisplayName());
            player.sendMessage(MessageManager.replaceAll(pMessage, vars));
        }
    }

    public static void playerJoinArena(Player player, String arenaname) {
        boolean found = false;
        boolean alreadyInGame = ArenaHandler.getArenaByPlayer(player) != null;

        if (!alreadyInGame) {
            for (Arena arena : MemoryStorage.arenaList) {
                if (arena.arenaName.equalsIgnoreCase(arenaname)) {
                    found = true;
                    if (arena.disguiseBlocks.isEmpty()) {
                        MessageManager.sendFMessage(player, ConfigC.error_joinNoBlocksSet);
                    } else {
                        boolean inventoryempty = true;
                        for (ItemStack invitem : player.getInventory()) {
                            if (invitem != null) {
                                if (invitem.getType() != Material.AIR) {
                                    inventoryempty = false;
                                }
                            }
                        }

                        for (ItemStack invitem : player.getInventory().getArmorContents()) {
                            if (invitem != null) {
                                if (invitem.getType() != Material.AIR) {
                                    inventoryempty = false;
                                }
                            }
                        }

                        if ((Boolean) MemoryStorage.config.get(ConfigC.requireInventoryClearOnJoin) && !inventoryempty) {
                            MessageManager.sendFMessage(player, ConfigC.error_joinInventoryNotEmpty);
                            return;
                        }

                        Location zero = new Location(Bukkit.getWorld(player.getWorld().getName()), 0, 0, 0, 0, 0);
                        if (arena.lobbyWarp != null && arena.hidersWarp != null && arena.seekersWarp != null && arena.spawnWarp != null) {
                            if (!arena.lobbyWarp.equals(zero) && !arena.hidersWarp.equals(zero) && !arena.seekersWarp.equals(zero) && !arena.spawnWarp.equals(zero)) {
                                if (arena.gameState == ArenaState.WAITING || arena.gameState == ArenaState.STARTING) {
                                    if (arena.playersInArena.size() >= arena.maxPlayers) {
                                        if (!PermissionsManager.hasPerm(player, PermissionsC.Permissions.joinfull, false)) {
                                            MessageManager.sendFMessage(player, ConfigC.error_joinFull);
                                            return;
                                        }
                                    }

                                    boolean canWarp = PlayerHandler.teleport(player, arena.lobbyWarp);
                                    if (!canWarp) {
                                        MessageManager.sendFMessage(player, ConfigC.error_teleportFailed);
                                        return;
                                    }

                                    System.out.println("[BlockHunt] " + player.getName() + " has joined " + arenaname);
                                    arena.playersInArena.add(player);
                                    JoinArenaEvent event = new JoinArenaEvent(player, arena);
                                    Bukkit.getPluginManager().callEvent(event);

                                    PlayerArenaData pad = new PlayerArenaData(player.getLocation(), player.getGameMode(), player.getInventory().getContents(), player
                                            .getInventory().getArmorContents(), player.getExp(), player.getLevel(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth(), player.getFoodLevel(),
                                            player.getActivePotionEffects(), player.getAllowFlight());

                                    MemoryStorage.pData.put(player, pad);

                                    player.setGameMode(GameMode.SURVIVAL);

                                    for (PotionEffect pe : player.getActivePotionEffects()) {
                                        player.removePotionEffect(pe.getType());
                                    }

                                    player.setFoodLevel(20);
                                    player.setHealth(20);
                                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                                    player.setLevel(arena.timer);
                                    player.setExp(0);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(new ItemStack(Material.AIR));
                                    player.getInventory().setChestplate(new ItemStack(Material.AIR));
                                    player.getInventory().setLeggings(new ItemStack(Material.AIR));
                                    player.getInventory().setBoots(new ItemStack(Material.AIR));
                                    player.setFlying(false);
                                    player.setAllowFlight(false);
                                    player.setWalkSpeed(0.2F);

                                    // Fix for client not showing players after
                                    // they join
                                    for (Player otherplayer : arena.playersInArena) {
                                        if (otherplayer.canSee(player))
                                            otherplayer.showPlayer(BlockHunt.plugin, player);
                                        // Make
                                        // new
                                        // player
                                        // visible
                                        // to
                                        // others
                                        if (player.canSee(otherplayer))
                                            player.showPlayer(BlockHunt.plugin, otherplayer);
                                        // Make
                                        // other
                                        // players
                                        // visible
                                        // to
                                        // new
                                        // player
                                    }

                                    if ((Boolean) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Enabled)) {
                                        if (MemoryStorage.shop.getFile().get(player.getName() + ".blockchooser") != null
                                                || PermissionsManager.hasPerm(player, PermissionsC.Permissions.shopblockchooser, false)) {
                                            ItemStack shopBlockChooser = new ItemStack(Material.getMaterial((String) MemoryStorage.config.get(ConfigC.shop_blockChooserv1IDname)), 1);
                                            ItemMeta shopBlockChooser_IM = shopBlockChooser.getItemMeta();
                                            shopBlockChooser_IM.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Name)));
                                            List<String> lores = MemoryStorage.config.getFile().getStringList(ConfigC.shop_blockChooserv1Description.location);
                                            List<String> lores2 = new ArrayList<>();
                                            for (String lore : lores) {
                                                lores2.add(MessageManager.replaceAll(lore));
                                            }
                                            shopBlockChooser_IM.setLore(lores2);
                                            shopBlockChooser.setItemMeta(shopBlockChooser_IM);

                                            player.getInventory().addItem(shopBlockChooser);


                                            ItemStack bedToQuit = new ItemStack(Material.RED_BED);
                                            ItemMeta meta = bedToQuit.getItemMeta();
                                            meta.setDisplayName(OnPlayerInteractEvent.DISPLAY_TO_QUIT);
                                            bedToQuit.setItemMeta(meta);

                                            player.getInventory().setItem(8, bedToQuit);
                                        }
                                    }

                                    if ((Boolean) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Enabled)) {
                                        if (MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") != 0) {
                                            ItemStack shopBlockHuntPass = new ItemStack(Material.getMaterial((String) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2IDName)),
                                                    1);
                                            ItemMeta shopBlockHuntPass_IM = shopBlockHuntPass.getItemMeta();
                                            shopBlockHuntPass_IM.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name)));
                                            List<String> lores = MemoryStorage.config.getFile().getStringList(ConfigC.shop_BlockHuntPassv2Description.location);
                                            List<String> lores2 = new ArrayList<>();
                                            for (String lore : lores) {
                                                lores2.add(MessageManager.replaceAll(lore));
                                            }

                                            shopBlockHuntPass_IM.setLore(lores2);
                                            shopBlockHuntPass.setItemMeta(shopBlockHuntPass_IM);
                                            shopBlockHuntPass.setAmount(MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass"));

                                            player.getInventory().addItem(shopBlockHuntPass);
                                        }
                                    }

                                    player.updateInventory();

                                    DisguiseAPI.undisguiseToAll(player);

                                    ArenaHandler.sendFMessage(arena, ConfigC.normal_joinJoinedArena, "playername-" + player.getName(),
                                            "1-" + arena.playersInArena.size(), "2-" + arena.maxPlayers);
                                    if (arena.playersInArena.size() < arena.minPlayers) {
                                        sendFMessage(arena, ConfigC.warning_lobbyNeedAtleast, "1-" + arena.minPlayers);
                                    }
                                } else {
                                    MessageManager.sendFMessage(player, ConfigC.error_joinArenaIngame);
                                }
                            } else {
                                MessageManager.sendFMessage(player, ConfigC.error_joinWarpsNotSet);
                            }
                        } else {
                            MessageManager.sendFMessage(player, ConfigC.error_joinWarpsNotSet);
                        }
                    }
                }
            }
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_joinAlreadyJoined);
            return;
        }

        if (!found) {
            MessageManager.sendFMessage(player, ConfigC.error_noArena, "name-" + arenaname);
        }

        SignsHandler.updateSigns();
    }

    public static void playerLeaveArena(Player player, boolean message, boolean cleanup) {
        Arena arena = ArenaHandler.getArenaByPlayer(player);

        if (arena != null) {
            System.out.println("[BlockHunt] " + player.getName() + " has left " + arena.arenaName);
            LeaveArenaEvent event = new LeaveArenaEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);

            if (cleanup) {
                arena.playersInArena.remove(player);
                arena.seekers.remove(player);
                MemoryStorage.lastTauntUsed.remove(player);
                if (arena.playersInArena.size() < arena.minPlayers && arena.gameState.equals(ArenaState.STARTING)) {
                    arena.gameState = ArenaState.WAITING;
                    arena.timer = 0;

                    sendFMessage(arena, ConfigC.warning_lobbyNeedAtleast, "1-" + arena.minPlayers);
                }

                if (arena.playersInArena.size() <= 1 && arena.gameState == ArenaState.STARTED) {
                    if (arena.seekers.size() >= arena.playersInArena.size()) {
                        ArenaHandler.seekersWin(arena);
                    } else {
                        ArenaHandler.hidersWin(arena);
                    }
                }

                if (arena.seekers.size() >= arena.playersInArena.size()) {
                    ArenaHandler.seekersWin(arena);
                }

                if (arena.seekers.size() <= 0 && arena.gameState == ArenaState.STARTED) {
                    Player seeker = arena.playersInArena.get(MemoryStorage.random.nextInt(arena.playersInArena.size()));
                    sendFMessage(arena, ConfigC.warning_ingameNEWSeekerChoosen, "seeker-" + seeker.getName());
                    sendFMessage(arena, ConfigC.normal_ingameSeekerChoosen, "seeker-" + seeker.getName());
                    DisguiseAPI.undisguiseToAll(seeker);
                    for (Player pl : arena.playersInArena) {
                        pl.showPlayer(BlockHunt.plugin, seeker);
                    }
                    seeker.getInventory().clear();
                    arena.seekers.add(seeker);
                    PlayerHandler.teleport(seeker, arena.seekersWarp);
                    MemoryStorage.seekertime.put(seeker, arena.waitingTimeSeeker / 2);
                    seeker.setWalkSpeed(0.3F);

                    // Fix for client not showing players after they join
                    for (Player otherplayer : arena.playersInArena) {
                        if (otherplayer.canSee(player))
                            otherplayer.showPlayer(BlockHunt.plugin, player); // Make new player
                        // visible to others
                        if (player.canSee(otherplayer))
                            player.showPlayer(BlockHunt.plugin, otherplayer); // Make other
                        // players visible
                        // to new player
                    }
                }
            }

            PlayerArenaData pad = new PlayerArenaData(null, null, null, null, null, null, null, null, null, null, false);

            if (MemoryStorage.pData.get(player) != null) {
                pad = MemoryStorage.pData.get(player);
            }

            player.getInventory().clear();
            player.getInventory().setContents(pad.pInventory);
            player.getInventory().setArmorContents(pad.pArmor);
            player.updateInventory();
            player.setExp(pad.pEXP);
            player.setLevel(pad.pEXPL);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(pad.pMaxHealth);
            player.setHealth(pad.pHealth);
            player.setFoodLevel(pad.pFood);
            player.addPotionEffects(pad.pPotionEffects);
            PlayerHandler.teleport(player, arena.spawnWarp);
            player.setGameMode(pad.pGameMode);
            player.setAllowFlight(pad.pFlying);
            if (player.getAllowFlight()) {
                player.setFlying(true);
            }
            player.setWalkSpeed(0.2F);

            MemoryStorage.pData.remove(player);
            MemoryStorage.choosenBlock.remove(player);

            for (Player pl : arena.playersInArena) {
                pl.showPlayer(BlockHunt.plugin, player);
                if (MemoryStorage.hiddenLoc.get(player) != null) {
                    if (MemoryStorage.hiddenLocWater.get(player) != null) {
                        Block pBlock = MemoryStorage.hiddenLoc.get(player).getBlock();
                        if (MemoryStorage.hiddenLocWater.get(player)) {
                            pl.sendBlockChange(pBlock.getLocation(), Bukkit.createBlockData(Material.WATER));
                        } else {
                            pl.sendBlockChange(pBlock.getLocation(), Bukkit.createBlockData(Material.AIR));
                        }
                    }
                }
            }

            DisguiseAPI.undisguiseToAll(player);

            ScoreboardHandler.removeScoreboard(player);

            MessageManager.sendFMessage(player, ConfigC.normal_leaveYouLeft);
            if (message) {
                ArenaHandler.sendFMessage(arena, ConfigC.normal_leaveLeftArena, "playername-" + player.getName(), "1-" + arena.playersInArena.size(), "2-"
                        + arena.maxPlayers);
            }
        } else {
            if (message) {
                MessageManager.sendFMessage(player, ConfigC.error_leaveNotInArena);
            }
            return;
        }

        SignsHandler.updateSigns();
    }

    public static void seekersWin(Arena arena) {
        String cause = "[BlockHunt] Seekers have won " + arena.arenaName;

        List<Player> winners = new ArrayList<>();
        List<Player> losers = new ArrayList<>();

        for (Player player : arena.playersInArena) {

            //arena.team.removeEntry(player.getName());

            if (arena.seekers.contains(player)) {

                winners.add(player);

                if (arena.seekersWinCommands != null) {
                    for (String command : arena.seekersWinCommands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getDisplayName()));
                    }

                    if (MemoryStorage.shop.getFile().get(player.getName() + ".tokens") == null) {
                        MemoryStorage.shop.getFile().set(player.getName() + ".tokens", 0);
                        MemoryStorage.shop.save();
                    }
                    int playerTokens = MemoryStorage.shop.getFile().getInt(player.getName() + ".tokens");
                    MemoryStorage.shop.getFile().set(player.getName() + ".tokens", playerTokens + arena.seekersTokenWin);
                    MemoryStorage.shop.save();

                    MessageManager.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + arena.seekersTokenWin);

                }

            } else {
                losers.add(player);
            }
        }

        EndArenaEvent event = new EndArenaEvent(winners, losers, arena);
        Bukkit.getServer().getPluginManager().callEvent(event);
        stopArena(arena, cause, ConfigC.normal_winSeekers);
    }

    public static void hidersWin(Arena arena) {
        String cause = "[BlockHunt] Hiders have won " + arena.arenaName;

        List<Player> winners = new ArrayList<>();
        List<Player> losers = new ArrayList<>();

        for (Player player : arena.playersInArena) {

            //arena.team.removeEntry(player.getName());

            if (arena.seekers.contains(player)) {
                losers.add(player);
            } else {
                winners.add(player);

                if (arena.hidersWinCommands != null) {
                    for (String command : arena.hidersWinCommands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getDisplayName()));
                    }

                    if (MemoryStorage.shop.getFile().get(player.getName() + ".tokens") == null) {
                        MemoryStorage.shop.getFile().set(player.getName() + ".tokens", 0);
                        MemoryStorage.shop.save();
                    }
                    int playerTokens = MemoryStorage.shop.getFile().getInt(player.getName() + ".tokens");
                    MemoryStorage.shop.getFile().set(player.getName() + ".tokens", playerTokens + arena.hidersTokenWin);
                    MemoryStorage.shop.save();

                    MessageManager.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + arena.hidersTokenWin);
                }
            }

        }

        EndArenaEvent event = new EndArenaEvent(winners, losers, arena);
        Bukkit.getServer().getPluginManager().callEvent(event);
        stopArena(arena, cause, ConfigC.normal_winHiders);
    }

    public static void stopArena(Arena arena, String cause, ConfigC message) {
        System.out.println(cause);
        ArenaHandler.sendFMessage(arena, message);
        arena.seekers.clear();

        for (Player player : arena.playersInArena) {
            playerLeaveArena(player, false, false);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }

        arena.gameState = ArenaState.WAITING;
        arena.timer = 0;
        arena.playersInArena.clear();
    }

    public static boolean noPlayersInArenas() {
        return MemoryStorage.arenaList.stream().noneMatch(arena -> arena.playersInArena.size() > 0);
    }

    public static Arena getArenaByPlayer(Player player) {
        return noPlayersInArenas() ? null : MemoryStorage.arenaList.stream().filter(arena -> arena.playersInArena.contains(player)).findFirst().orElse(null);
    }
}
