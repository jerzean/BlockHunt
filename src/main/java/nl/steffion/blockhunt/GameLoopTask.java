package nl.steffion.blockhunt;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.FallingBlockWatcher;
import nl.steffion.blockhunt.Listeners.PlayerTauntEvent;
import nl.steffion.blockhunt.Managers.MessageManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameLoopTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Arena arena : MemoryStorage.arenaList) {
            if (arena.playersInArena.size() > 0) {
                if (arena.gameState == Arena.ArenaState.WAITING) {
                    if (arena.playersInArena.size() >= arena.minPlayers) {
                        arena.gameState = Arena.ArenaState.STARTING;
                        arena.timer = arena.timeInLobbyUntilStart;
                        ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaIsStarting, "1-" + arena.timeInLobbyUntilStart);
                    }
                } else if (arena.gameState == Arena.ArenaState.STARTING) {
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
                        arena.gameState = Arena.ArenaState.STARTED;
                        arena.timer = arena.gameTime;
                        ArenaHandler.sendFMessage(arena, ConfigC.normal_lobbyArenaStarted, "secs-" + arena.waitingTimeSeeker);

                        //arena.playersInArena.forEach(player -> arena.team.addEntry(player.getName()));

                        //seekers init
                        for (int i = arena.amountSeekersOnStart; i > 0; i = i - 1) {

                            boolean loop = true;
                            Player seeker = arena.getSeeker(player -> !Arena.lastSeekers.equals(player.getUniqueId()));

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
                                    Arena.lastSeekers = seeker.getUniqueId();
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

                                ItemStack blockCount = new ItemStack(block.getType(), 5);
                                arenaPlayer.getInventory().setItem(8, blockCount);

                                MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, block.getType());
                                ((FallingBlockWatcher) disguise.getWatcher()).setGridLocked(false);
                                disguise.getWatcher().setNoGravity(true);
                                DisguiseAPI.disguiseToPlayers(arenaPlayer, disguise, arena.playersInArena);

                                PlayerHandler.teleport(arenaPlayer, arena.hidersWarp);

                                arenaPlayer.getInventory().setHelmet(new ItemStack(block));

                                MemoryStorage.pBlock.put(arenaPlayer, block);
                                String blockName = block.getType().name();
                                blockName = WordUtils.capitalizeFully(blockName.replace("_", " "));
                                MessageManager.sendFMessage(arenaPlayer, ConfigC.normal_ingameBlock, "block-" + blockName);

                                arenaPlayer.updateInventory();
                            }
                        }
                    }
                }

                //If game has been started
                if (arena.gameState == Arena.ArenaState.STARTED) {
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

                            Bukkit.getScheduler().runTaskTimer(BlockHunt.plugin, bukkitTask -> {
                                if (arena.gameState != Arena.ArenaState.STARTED) {
                                    bukkitTask.cancel();
                                    return;
                                }

                                ArrayList<Material> newRemainingBlocks = new ArrayList<>();

                                for (Player arenaPlayer : arena.playersInArena) {
                                    if (!arena.seekers.contains(arenaPlayer)) {
                                        Material block = arenaPlayer.getInventory().getItem(8).getType();
                                        if (!newRemainingBlocks.contains(block)) { //Don't print double up block names.
                                            newRemainingBlocks.add(block);
                                        }
                                    }
                                }

                                if (remainingBlocks.size() != newRemainingBlocks.size()) {
                                    index.set(0);
                                    remainingBlocks.clear();
                                    remainingBlocks.addAll(newRemainingBlocks);
                                }

                                if (index.incrementAndGet() >= remainingBlocks.size())
                                    index.set(0);

                                Material remainingMat = remainingBlocks.get(index.get());

                                int count = Math.toIntExact(arena.playersInArena.stream().filter(player -> !arena.seekers.contains(player) && player.getInventory().getItem(8).getType() == remainingMat).count());

                                ItemStack remainingBlock = new ItemStack(remainingMat, count);
                                ItemMeta meta = remainingBlock.getItemMeta();
                                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + "Remaining: " + ChatColor.BOLD + "" + ChatColor.RED + count);
                                remainingBlock.setItemMeta(meta);
                                arena.seekers.forEach(player -> player.getInventory().setItem(8, remainingBlock));

                            }, 1, 10);

                            StringBuilder builder = new StringBuilder(ChatColor.GREEN + "Remaining Block: ");

                            for (Material material : remainingBlocks)
                                builder.append(ChatColor.BLUE)
                                        .append(WordUtils.capitalizeFully(material.name().toLowerCase().replace("_", " ")))
                                        .append(", ");
                            String msg = builder.toString();
                            msg = msg.substring(0, msg.length() - 2);
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
                        boolean spawnSeeker = false;
                        //Teleport seekers and set Item
                        for (Player player : arena.seekers) {
                            if (player.getInventory().getItem(0) == null || player.getInventory().getItem(0).getType() != Material.DIAMOND_SWORD) {
                                player.getInventory().clear();
                                player.setSaturation(10);
                                ItemStack i = new ItemStack(Material.DIAMOND_SWORD, 1);
                                i.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
                                ItemMeta meta = i.getItemMeta();
                                meta.setUnbreakable(true);
                                meta.addItemFlags(ItemFlag.values());
                                i.setItemMeta(meta);
                                player.getInventory().setItem(0, i);
                                player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
                                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
                                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
                                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
                            }


                            if (MemoryStorage.seekertime.get(player) != null) {
                                MemoryStorage.seekertime.put(player, MemoryStorage.seekertime.get(player) - 1);
                                if (MemoryStorage.seekertime.get(player) <= 0) {
                                    spawnSeeker = true;
                                    PlayerHandler.teleport(player, arena.hidersWarp);
                                    MemoryStorage.seekertime.remove(player);
                                    ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameSeekerSpawned, "playername-" + player.getName());
                                }
                            }
                        }

                        if (spawnSeeker) {
                            ItemStack tauntItem = new ItemStack(Material.EMERALD);
                            ItemMeta tauntMeta = tauntItem.getItemMeta();
                            tauntMeta.setDisplayName(PlayerTauntEvent.DISPLAY_NAME);
                            tauntItem.setItemMeta(tauntMeta);

                            for (Player player : arena.playersInArena) {
                                if (!arena.seekers.contains(player)) {
                                    ItemStack stack = player.getInventory().getItem(4);
                                    if (stack == null || !stack.isSimilar(tauntItem))
                                        player.getInventory().setItem(4, tauntItem.clone());
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
                                        if (pBlockData.getMaterial() == Material.WATER)
                                            MemoryStorage.hiddenLocWater.put(player, true);
                                        else
                                            MemoryStorage.hiddenLocWater.put(player, false);

                                        if (DisguiseAPI.isDisguised(player)) {
                                            DisguiseAPI.undisguiseToAll(player);

                                            for (Player pl : arena.playersInArena) {
                                                if (!pl.equals(player)) {
                                                    pl.hidePlayer(BlockHunt.plugin, player);
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
                                                pl.hidePlayer(BlockHunt.plugin, player);
                                                pl.sendBlockChange(pBlock.getLocation(), itemBlock.getType().createBlockData());
                                            }
                                        }
                                    } else
                                        MessageManager.sendFMessage(player, ConfigC.warning_ingameNoSolidPlace);
                                }
                            }
                        }
                    } else {
                        //Hiders Win !
                        ArenaHandler.hidersWin(arena);
                        return;
                    }
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
            ScoreboardHandler.updateScoreboard(arena);
        }
        SignsHandler.updateSigns(); //TODO Only do this when needed (gamestate change or player count change)
    }
}
