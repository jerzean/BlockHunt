package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Arena.ArenaState;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class OnPlayerMoveEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        // Early exit if no one is in any arena
        if (ArenaHandler.noPlayersInArenas()) return;
        Player player = event.getPlayer();
        for (Arena arena : MemoryStorage.arenaList) {
            if (arena.playersInArena.contains(player)) {
                if (arena.gameState == ArenaState.INGAME) {
                    if (!arena.seekers.contains(player)) {
                        Location moveLocation = event.getTo() == null ? event.getFrom() : event.getTo();
                        if (MemoryStorage.hiddenLoc.containsKey(player)) {
                            Block currentBlock = player.getLocation().getBlock();
                            Block moveBlock = moveLocation.getBlock();

                            if (currentBlock.getX() != moveBlock.getX() && currentBlock.getY() != moveBlock.getY() && currentBlock.getZ() != moveBlock.getZ()) {
                                SolidBlockHandler.makePlayerUnsolid(player);
                                ItemStack itemBlock = player.getInventory().getItem(8);

                                if (itemBlock == null) {
                                    if (MemoryStorage.pBlock.get(player) != null) {
                                        itemBlock = MemoryStorage.pBlock.get(player);
                                        player.getInventory().setItem(8, itemBlock);
                                        player.updateInventory();
                                    }
                                }

                                itemBlock.setAmount(5);
                            }
                        }
                    }


                    if (arena.pos1 == null || arena.pos2 == null) {
                        BlockHunt.plugin.getLogger().info("Arena:" +
                                arena.arenaName + " appears to have bad coords : pos1:" +
                                ((arena.pos1 != null) ? arena.pos1.toString() : " NULL") + " Pos2:" +
                                ((arena.pos2 != null) ? arena.pos2.toString() : " NULL"));
                        BlockHunt.plugin.getLogger().info("Player has been returned to hiderswarp due to bad arena state");
                        //event.setCancelled(true);
                        Location loc = player.getLocation();
                        player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                        player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                        PlayerHandler.teleport(player, arena.hidersWarp);
                        return;
                    }

                    double maxX = Math.max(arena.pos1.getX(), arena.pos2.getX());
                    double minX = Math.min(arena.pos1.getX(), arena.pos2.getX());
                    double maxY = Math.max(arena.pos1.getY(), arena.pos2.getY());
                    double minY = Math.min(arena.pos1.getY(), arena.pos2.getY());
                    double maxZ = Math.max(arena.pos1.getZ(), arena.pos2.getZ());
                    double minZ = Math.min(arena.pos1.getZ(), arena.pos2.getZ());

                    Location loc = player.getLocation();
                    if (loc.getBlockX() > maxX) {
                        //event.setCancelled(true);
                        player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                        player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                        PlayerHandler.teleport(player, arena.hidersWarp);
                    } else if (loc.getBlockX() < minX) {
                        //event.setCancelled(true);
                        player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                        player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                        PlayerHandler.teleport(player, arena.hidersWarp);
                    } else if (loc.getBlockZ() > maxZ) {
                        player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                        player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                        PlayerHandler.teleport(player, arena.hidersWarp);
                    } else if (loc.getBlockZ() < minZ) {
                        //event.setCancelled(true);
                        player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                        player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                        PlayerHandler.teleport(player, arena.hidersWarp);
                    } else if (loc.getBlockY() > maxY) {
                        //event.setCancelled(true);
                        player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                        player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                        PlayerHandler.teleport(player, arena.hidersWarp);
                    } else if (loc.getBlockY() < minY) {
                        //event.setCancelled(true);
                        player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                        player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                        PlayerHandler.teleport(player, arena.hidersWarp);
                    }
                }
            }
        }
    }
}
