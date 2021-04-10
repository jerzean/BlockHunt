package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Arena.ArenaState;
import nl.Steffion.BlockHunt.Managers.MessageManager;
import nl.Steffion.BlockHunt.Managers.PermissionsManager;
import nl.Steffion.BlockHunt.PermissionsC.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnPlayerInteractEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (PermissionsManager.hasPerm(player, Permissions.create, false)) {
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if (item.getType() != Material.AIR) {
                if (meta != null && meta.hasDisplayName()) {
                    if (meta.getDisplayName().equals(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.wandName)))) {
                        Action action = event.getAction();
                        if (block != null) {
                            Location location = block.getLocation();
                            if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                                event.setCancelled(true);
                                if (MemoryStorage.pos1.get(player) == null || !MemoryStorage.pos1.get(player).equals(location)) {
                                    MessageManager.sendFMessage(player, ConfigC.normal_wandSetPosition, "number-1",
                                            "pos-%N(%A" + location.getBlockX() + "%N, %A" + location.getBlockY() + "%N, %A" + location.getBlockZ() + "%N)", "x-"
                                                    + location.getBlockX(), "y-" + location.getBlockY(), "z-" + location.getBlockZ());
                                    MemoryStorage.pos1.put(player, location);
                                }
                            } else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                                event.setCancelled(true);
                                if (MemoryStorage.pos2.get(player) == null || !MemoryStorage.pos2.get(player).equals(location)) {
                                    MessageManager.sendFMessage(player, ConfigC.normal_wandSetPosition, "number-2",
                                            "pos-%N(%A" + location.getBlockX() + "%N, %A" + location.getBlockY() + "%N, %A" + location.getBlockZ() + "%N)", "x-"
                                                    + location.getBlockX(), "y-" + location.getBlockY(), "z-" + location.getBlockZ());
                                    MemoryStorage.pos2.put(player, location);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (block != null) {
                if (block instanceof Sign) {
                    if (SignsHandler.isSign(block.getLocation())) {
                        Sign sign = (Sign) block.getState();
                        if (sign.getLine(1).equals(MessageManager.replaceAll(MemoryStorage.config.getFile().getStringList(ConfigC.sign_LEAVE.location).get(1)))) {
                            if (PermissionsManager.hasPerm(player, Permissions.joinsign, true)) {
                                ArenaHandler.playerLeaveArena(player, true, true);
                            }
                        } else if (sign.getLine(1).equals(MessageManager.replaceAll(MemoryStorage.config.getFile().getStringList(ConfigC.sign_SHOP.location).get(1)))) {
                            if (PermissionsManager.hasPerm(player, Permissions.shop, true)) {
                                InventoryHandler.openShop(player);
                            }
                        } else {
                            for (Arena arenaGame : MemoryStorage.arenaList) {
                                if (sign.getLines()[1].contains(arenaGame.arenaName)) {
                                    if (PermissionsManager.hasPerm(player, Permissions.joinsign, true)) {
                                        ArenaHandler.playerJoinArena(player, arenaGame.arenaName);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Arena arena = ArenaHandler.getArenaByPlayer(player);

        if (arena != null) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (block != null && block.getType() != Material.AIR) {
                    if (block.getType().equals(Material.ENCHANTING_TABLE) || block.getType().equals(Material.CRAFTING_TABLE)
                            || block.getType().equals(Material.FURNACE) || block.getType().equals(Material.CHEST)
                            || block.getType().equals(Material.ANVIL) || block.getType().equals(Material.ENDER_CHEST)
                            || block.getType().equals(Material.JUKEBOX) || block.getRelative(event.getBlockFace()).getType().equals(Material.FIRE)) {
                        event.setCancelled(true);
                    }
                }
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK && block != null) {
                for (Player pl : arena.playersInArena) {
                    if (MemoryStorage.hiddenLoc.get(pl) != null) {
                        Block moveLocBlock = MemoryStorage.hiddenLoc.get(pl).getBlock();
                        if (block != null &&
                                moveLocBlock.getX() == block.getX() &&
                                moveLocBlock.getY() == block.getY() &&
                                moveLocBlock.getZ() == block.getZ()) {
                            pl.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
                            SolidBlockHandler.makePlayerUnsolid(pl);
                        }
                    }
                }
            }

            if (arena.gameState.equals(ArenaState.WAITING) || arena.gameState.equals(ArenaState.STARTING)) {
                event.setCancelled(true);
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() != Material.AIR) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        if (meta.getDisplayName().equals(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Name)))) {
                            Inventory blockChooser = Bukkit.createInventory(null, 36, MessageManager.replaceAll("\u00A7r" + MemoryStorage.config.get(ConfigC.shop_blockChooserv1Name)));
                            if (arena.disguiseBlocks != null) {
                                for (int i = arena.disguiseBlocks.size(); i > 0; i = i - 1) {
                                    blockChooser.setItem(i - 1, arena.disguiseBlocks.get(i - 1));
                                }
                            }

                            player.openInventory(blockChooser);
                        }

                        if (meta.getDisplayName().equals(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name)))) {
                            Inventory inventoryPass = Bukkit.createInventory(null, 9, MessageManager.replaceAll("\u00A7r" + MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name)));
                            ItemStack itemStack = new ItemStack(Material.BLUE_WOOL, 1);
                            ItemMeta itemHuntPass = itemStack.getItemMeta();
                            itemHuntPass.setDisplayName(MessageManager.replaceAll("&eSEEKER"));
                            itemStack.setItemMeta(itemHuntPass);
                            inventoryPass.setItem(1, itemStack);

                            ItemStack passHider = new ItemStack(Material.RED_WOOL, 1);
                            itemHuntPass.setDisplayName(MessageManager.replaceAll("&eHIDER"));
                            passHider.setItemMeta(itemHuntPass);
                            inventoryPass.setItem(7, passHider);

                            player.openInventory(inventoryPass);
                        }
                    }
                }
            }
        }
    }
}
