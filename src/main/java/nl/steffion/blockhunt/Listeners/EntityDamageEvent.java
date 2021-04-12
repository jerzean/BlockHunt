package nl.steffion.blockhunt.Listeners;

import me.libraryaddict.disguise.DisguiseAPI;
import nl.steffion.blockhunt.*;
import nl.steffion.blockhunt.Managers.MessageManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityDamageEvent implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                Arena arena = ArenaHandler.getArenaByPlayer(shooter);

                if (arena != null && event.getHitBlock() != null && arena.seekers.contains(shooter)) {
                    Block hitBlock = event.getHitBlock();
                    for (Player player : arena.playersInArena) {
                        if (!arena.seekers.contains(player) && MemoryStorage.hiddenLoc.containsKey(player)) {
                            Block hiddenBlock = MemoryStorage.hiddenLoc.get(player).getBlock();
                            if (hitBlock.getX() == hiddenBlock.getX() && hiddenBlock.getY() == hitBlock.getY() && hitBlock.getZ() == hiddenBlock.getZ()) {
                                World world = event.getEntity().getWorld();
                                world.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 2, 1);
                                SolidBlockHandler.makePlayerUnsolid(player);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        // Early exit if no one is in any arena
        if (ArenaHandler.noPlayersInArenas()) return;

        if (!(event.getEntity() instanceof Player)) {
            // We only care about player damage
            return;
        }

        Player player = (Player) event.getEntity();
        Player damager = null;

        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else {
            if ((event.getCause() == DamageCause.PROJECTILE) && (event.getDamager() instanceof Arrow)) {
                // If damage was caused by an arrow, find out who shot the arrow
                Arrow arrow = (Arrow) event.getDamager();
                ProjectileSource shooter = arrow.getShooter();
                if (shooter instanceof Player) {
                    damager = (Player) shooter;
                }
            }
        }

        Arena arena = ArenaHandler.getArenaByPlayer(player);

        if (arena != null) {
            if (damager == null && !arena.seekers.contains(player)) {
                event.setCancelled(true);
                return;
            }

            if (arena.gameState == Arena.ArenaState.WAITING || arena.gameState == Arena.ArenaState.STARTING) {
                // Always cancel damage when players are waiting
                event.setCancelled(true);
            } else {
                // Seeker receiving damage
                if (arena.seekers.contains(player)) {
                    if (arena.seekers.contains(damager)) {
                        // Seeker damaged by seeker
                        if (!arena.seekersCanHurtSeekers) {
                            event.setCancelled(true);
                            return;
                        }
                    } else {
                        // Seeker damaged by hider
                        if (!arena.hidersCanHurtSeekers) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                } else {
                    // Hider damaged by hider
                    if (!arena.hidersCanHurtHiders && !arena.seekers.contains(damager)) {
                        event.setCancelled(true);
                        return;
                    }
                }

                // The damage is allowed, so lets handle it!
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);

                if (event.getDamage() >= player.getHealth()) {
                    player.setHealth(20);
                    event.setCancelled(true);

                    // try moving everything to the next tick to prevent "Removing entity while ticking" errors
                    Player finalDamager = damager;
                    player.getServer().getScheduler().runTask(BlockHunt.plugin, () -> {
                        DisguiseAPI.undisguiseToAll(player);
                        MemoryStorage.pBlock.remove(player);

                        if (!arena.seekers.contains(player)) {
                            if (MemoryStorage.shop.getFile().get(finalDamager.getName() + ".tokens") == null) {
                                MemoryStorage.shop.getFile().set(finalDamager.getName() + ".tokens", 0);
                                MemoryStorage.shop.save();
                            }
                            int damagerTokens = MemoryStorage.shop.getFile().getInt(finalDamager.getName() + ".tokens");
                            MemoryStorage.shop.getFile().set(finalDamager.getName() + ".tokens", damagerTokens + arena.killTokens);
                            MemoryStorage.shop.save();

                            MessageManager.sendFMessage(finalDamager, ConfigC.normal_addedToken, "amount-" + arena.killTokens);

                            if (MemoryStorage.shop.getFile().get(player.getName() + ".tokens") == null) {
                                MemoryStorage.shop.getFile().set(player.getName() + ".tokens", 0);
                                MemoryStorage.shop.save();
                            }
                            int playerTokens = MemoryStorage.shop.getFile().getInt(player.getName() + ".tokens");
                            float addingTokens = ((float) arena.hidersTokenWin - (((float) arena.timer / (float) arena.gameTime) * (float) arena.hidersTokenWin));
                            MemoryStorage.shop.getFile().set(player.getName() + ".tokens", playerTokens + (int) addingTokens);
                            MemoryStorage.shop.save();

                            MessageManager.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + (int) addingTokens);

                            arena.seekers.add(player);
                            player.setWalkSpeed(0.3F);
                            ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameHiderDied, "playername-" + player.getDisplayName(), "killer-" + finalDamager.getDisplayName());

                            int hidercount = (arena.playersInArena.size() - arena.seekers.size());
                            if ((hidercount <= 3) && (hidercount > 0)) {
                                List<String> hiders = new ArrayList<>();
                                for (Player p : arena.playersInArena) {
                                    if (!arena.seekers.contains(p)) {
                                        hiders.add(p.getName());
                                    }
                                }
                                Collections.sort(hiders);
                                ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameHidersLeft, "left-" + StringUtils.join(hiders.toArray(), ", "));
                            } else {
                                ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameHidersLeft, "left-" + hidercount);
                            }
                        }

                        player.getInventory().clear();
                        player.updateInventory();

                        if (arena.seekers.size() >= arena.playersInArena.size()) {
                            ArenaHandler.seekersWin(arena);
                        } else {
                            DisguiseAPI.undisguiseToAll(player);
                            MemoryStorage.seekertime.put(player, arena.waitingTimeSeeker / 2);
                            PlayerHandler.teleport(player, arena.seekersWarp);
                            player.setGameMode(GameMode.SURVIVAL);
                            player.setWalkSpeed(0.3F);

                            // Fix for client not showing players after they join
                            for (Player otherplayer : arena.playersInArena) {
                                if (otherplayer.canSee(player))
                                    otherplayer.showPlayer(BlockHunt.plugin, player); // Make new player visible to others
                                if (player.canSee(otherplayer))
                                    player.showPlayer(BlockHunt.plugin, otherplayer); // Make other players visible to new player
                            }
                        }
                    });
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamageEvent(org.bukkit.event.entity.EntityDamageEvent event) {
        // Early exit if no one is in any arena
        if (ArenaHandler.noPlayersInArenas()) return;

        Entity ent = event.getEntity();
        if (ent instanceof Player) {
            Player player = (Player) event.getEntity();
            Arena arena = ArenaHandler.getArenaByPlayer(player);

            if (arena != null) {
                DamageCause cause = event.getCause();
                switch (cause) {
                    case ENTITY_ATTACK:
                    case PROJECTILE:
                        // Do nothing about damage from an entity
                        // Any entity damage that makes it to here was already allowed by the EntityDamageByEntity event
                        break;
                    case FALL:
                        // Should we prevent the fall damage?
                        if (arena.seekers.contains(player)) {
                            if (!arena.seekersTakeFallDamage) {
                                // Prevent seeker fall damage (if configured)
                                event.setCancelled(true);
                                return;
                            }
                        } else {
                            if (!arena.hidersTakeFallDamage) {
                                // Prevent hider fall damage (if configured)
                                event.setCancelled(true);
                                return;
                            }
                        }
                        break;

                    case CUSTOM: {
                        if (arena.seekers.contains(player) && event.getDamage() >= player.getHealth()) {
                            player.setHealth(20);
                            PlayerHandler.teleport(player, arena.seekersWarp);
                            event.setCancelled(true);
                        }
                    }
                    break;
                    default:
                        // Cancel all non-entity damage for all players (lava, drowning, fire, etc)
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }
}
