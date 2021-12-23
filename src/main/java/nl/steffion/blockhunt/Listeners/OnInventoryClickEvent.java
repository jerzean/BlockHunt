package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.*;
import nl.steffion.blockhunt.Managers.MessageManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnInventoryClickEvent implements Listener {

    public static void updownButton(Player player, ItemStack item, Arena arena, Arena.ArenaType at, int option, int max, int min, int add, int remove) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains((String) MemoryStorage.messages.get(ConfigC.button_add2))) {
            if (option < max) {
                switch (at) {
                    case maxPlayers:
                        arena.maxPlayers = option + add;
                        break;
                    case minPlayers:
                        arena.minPlayers = option + add;
                        break;
                    case amountSeekersOnStart:
                        arena.amountSeekersOnStart = option + add;
                        break;
                    case timeInLobbyUntilStart:
                        arena.timeInLobbyUntilStart = option + add;
                        break;
                    case waitingTimeSeeker:
                        arena.waitingTimeSeeker = option + add;
                        break;
                    case gameTime:
                        arena.gameTime = option + add;
                        break;
                    case blockAnnouncerTime:
                        arena.blockAnnouncerTime = option + add;
                        break;
                    case timeUntilHidersSword:
                        arena.timeUntilHidersSword = option + add;
                        break;
                    case hidersTokenWin:
                        arena.hidersTokenWin = option + add;
                        break;
                    case seekersTokenWin:
                        arena.seekersTokenWin = option + add;
                        break;
                    case killTokens:
                        arena.killTokens = option + add;
                        break;
                }
            } else {
                MessageManager.sendFMessage(player, ConfigC.error_setTooHighNumber, "max-" + max);
            }
        } else if (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains((String) MemoryStorage.messages.get(ConfigC.button_remove2))) {
            if (option > min) {
                switch (at) {
                    case maxPlayers:
                        arena.maxPlayers = option - remove;
                        break;
                    case minPlayers:
                        arena.minPlayers = option - remove;
                        break;
                    case amountSeekersOnStart:
                        arena.amountSeekersOnStart = option - remove;
                        break;
                    case timeInLobbyUntilStart:
                        arena.timeInLobbyUntilStart = option - remove;
                        break;
                    case waitingTimeSeeker:
                        arena.waitingTimeSeeker = option - remove;
                        break;
                    case gameTime:
                        arena.gameTime = option - remove;
                        break;
                    case blockAnnouncerTime:
                        arena.blockAnnouncerTime = option - remove;
                        break;
                    case timeUntilHidersSword:
                        arena.timeUntilHidersSword = option - remove;
                        break;
                    case hidersTokenWin:
                        arena.hidersTokenWin = option - remove;
                        break;
                    case seekersTokenWin:
                        arena.seekersTokenWin = option - remove;
                        break;
                    case killTokens:
                        arena.killTokens = option - remove;
                        break;
                }
            } else {
                MessageManager.sendFMessage(player, ConfigC.error_setTooLowNumber, "min-" + min);
            }
        }
    }

    public static void booleanButton(Player player, ItemStack stack, Arena arena, Arena.ArenaType arenaType) {
        switch (arenaType) {
            case hidersPVP: {
                arena.hidersCanHurtSeekers = !arena.hidersCanHurtSeekers;
                stack.setType(arena.hidersCanHurtSeekers ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                break;
            }
            case seekerAttackSeeker: {
                arena.seekersCanHurtSeekers = !arena.seekersCanHurtSeekers;
                stack.setType(arena.seekersCanHurtSeekers ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Arena arena = ArenaHandler.getArenaByPlayer(player);

        Inventory inv = event.getInventory();
        InventoryView invView = event.getView();

        if (arena != null)
            event.setCancelled(true);

        if (inv.getType().equals(InventoryType.CHEST)) {
            if (invView.getTitle().contains("DisguiseBlocks")) {
                if (event.getCurrentItem() != null) {
                    if (!event.getCurrentItem().getType().isBlock()) {
                        if (!event.getCurrentItem().getType().equals(Material.FLOWER_POT)) {
                            event.setCancelled(true);
                            MessageManager.sendFMessage(player, ConfigC.error_setNotABlock);
                        }
                    }
                }
                return;
            }

            // Early exit if this isnt a blockhunt inventory
            if (!invView.getTitle().contains("BlockHunt"))
                return;


            if (invView.getTitle().startsWith("\u00A7r")) {
                if (invView.getTitle().equals(MessageManager.replaceAll("\u00A7r" + MemoryStorage.config.get(ConfigC.shop_title)))) {
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    if (MemoryStorage.shop.getFile().get(player.getName() + ".tokens") == null) {
                        MemoryStorage.shop.getFile().set(player.getName() + ".tokens", 0);
                        MemoryStorage.shop.save();
                    }
                    int playerTokens = MemoryStorage.shop.getFile().getInt(player.getName() + ".tokens");
                    if (item == null)
                        return;
                    if (item.getType().equals(Material.AIR))
                        return;
                    if (item.getItemMeta().getDisplayName() == null)
                        return;
                    if (item.getItemMeta().getDisplayName().equals(MessageManager.replaceAll(MemoryStorage.config.get(ConfigC.shop_blockChooserv1Name).toString()))) {
                        if (playerTokens >= (Integer) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Price)) {
                            MemoryStorage.shop.getFile().set(player.getName() + ".blockchooser", true);
                            MemoryStorage.shop.getFile().set(player.getName() + ".tokens", playerTokens - (Integer) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Price));
                            MemoryStorage.shop.save();
                            MessageManager.sendFMessage(player, ConfigC.normal_shopBoughtItem, "itemname-" + MemoryStorage.config.get(ConfigC.shop_blockChooserv1Name));
                        } else {
                            MessageManager.sendFMessage(player, ConfigC.error_shopNeedMoreTokens);
                        }
                    } else if (item.getItemMeta().getDisplayName().equals(MessageManager.replaceAll(MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name).toString()))) {
                        if (playerTokens >= (Integer) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Price)) {
                            if (MemoryStorage.shop.getFile().get(player.getName() + ".blockhuntpass") == null) {
                                MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", 0);
                                MemoryStorage.shop.save();
                            }

                            MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", (Integer) MemoryStorage.shop.getFile().get(player.getName() + ".blockhuntpass") + 1);
                            MemoryStorage.shop.getFile().set(player.getName() + ".tokens", playerTokens - (Integer) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Price));
                            MemoryStorage.shop.save();
                            MessageManager.sendFMessage(player, ConfigC.normal_shopBoughtItem, "itemname-" + MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name));
                        } else {
                            MessageManager.sendFMessage(player, ConfigC.error_shopNeedMoreTokens);
                        }
                    }

                    InventoryHandler.openShop(player);
                } else if (invView.getTitle().contains(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Name)))) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getType().isBlock()) {
                            MemoryStorage.choosenBlock.put(player, event.getCurrentItem());
                            String blockName = event.getCurrentItem().getType().name();
                            blockName = WordUtils.capitalizeFully(blockName.replace("_", " "));
                            MessageManager.sendFMessage(player, ConfigC.normal_shopChoosenBlock, "block-"
                                    + blockName);
                        } else {
                            MessageManager.sendFMessage(player, ConfigC.error_setNotABlock);
                        }
                    }
                } else if (invView.getTitle().contains(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name))) && arena != null) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getType().equals(Material.BLUE_WOOL)) {
                            int i = 0;
                            for (Player playerCheck : arena.playersInArena) {
                                if (MemoryStorage.choosenSeeker.get(playerCheck) != null) {
                                    if (MemoryStorage.choosenSeeker.get(playerCheck)) {
                                        i = i + 1;
                                    }
                                }
                            }

                            if (i >= arena.amountSeekersOnStart) {
                                MessageManager.sendFMessage(player, ConfigC.error_shopMaxSeekersReached);
                            } else {
                                MemoryStorage.choosenSeeker.put(player, true);
                                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                player.updateInventory();
                                MessageManager.sendFMessage(player, ConfigC.normal_shopChoosenSeeker);
                                inv.clear();
                                if (MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") == 1) {
                                    MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", null);
                                } else {
                                    MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") - 1);
                                }
                                MemoryStorage.shop.save();
                            }


                        } else if (event.getCurrentItem().getType().equals(Material.RED_WOOL)) {
                            int i = 0;
                            if (arena.playersInArena.contains(player)) {
                                for (Player playerCheck : arena.playersInArena) {
                                    if (MemoryStorage.choosenSeeker.get(playerCheck) != null) {
                                        if (!MemoryStorage.choosenSeeker.get(playerCheck)) {
                                            i = i + 1;
                                        }
                                    }
                                }
                            }

                            if (i >= (arena.playersInArena.size() - 1)) {
                                MessageManager.sendFMessage(player, ConfigC.error_shopMaxHidersReached);
                            } else {
                                MemoryStorage.choosenSeeker.put(player, false);
                                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                player.updateInventory();
                                MessageManager.sendFMessage(player, ConfigC.normal_shopChoosenHiders);
                                inv.clear();
                                if (MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") == 1) {
                                    MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", null);
                                } else {
                                    MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") - 1);
                                }
                                MemoryStorage.shop.save();
                            }
                        }
                    }
                } else {
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    String arenaname = inv.getItem(0).getItemMeta().getDisplayName().replaceAll(MessageManager.replaceAll("%NBlockHunt arena: %A"), "");

                    Arena arenaInv = null;
                    for (Arena arena2 : MemoryStorage.arenaList) {
                        if (arena2.arenaName.equalsIgnoreCase(arenaname)) {
                            arenaInv = arena2;
                        }
                    }

                    if (arenaInv == null)
                        return;

                    if (item == null)
                        return;

                    ItemMeta itemMeta = item.getItemMeta();

                    if (itemMeta == null)
                        return;

                    if (item.getType().equals(Material.AIR))
                        return;

                    Material type = item.getType();

                    if (!itemMeta.hasDisplayName())
                        return;

                    String displayName = itemMeta.getDisplayName();

                    if (type.equals(Material.GOLD_NUGGET)) {
                        if (displayName.contains("maxPlayers")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.maxPlayers, arenaInv.maxPlayers, 99, 2, 1, 1);
                        } else if (displayName.contains("minPlayers")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.minPlayers, arenaInv.minPlayers, 99 - 1, 2, 1, 1);
                        } else if (displayName.contains("amountSeekersOnStart")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.amountSeekersOnStart, arenaInv.amountSeekersOnStart, arenaInv.maxPlayers - 1, 1, 1, 1);
                        } else if (displayName.contains("timeInLobbyUntilStart")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.timeInLobbyUntilStart, arenaInv.timeInLobbyUntilStart, 1000, 5, 5, 5);
                        } else if (displayName.contains("waitingTimeSeeker")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.waitingTimeSeeker, arenaInv.waitingTimeSeeker, 1000, 5, 5, 5);
                        } else if (displayName.contains("gameTime")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.gameTime, arenaInv.gameTime, 1000, 5, 5, 5);
                        } else if (displayName.contains("blockAnnouncerTime")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.blockAnnouncerTime, arenaInv.blockAnnouncerTime, 1000, 0, 5, 5);
                        } else if (displayName.contains("timeUntilHidersSword")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.timeUntilHidersSword, arenaInv.timeUntilHidersSword, 1000, 0, 5, 5);
                        } else if (displayName.contains("hidersTokenWin")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.hidersTokenWin, arenaInv.hidersTokenWin, 1000, 0, 1, 1);
                        } else if (displayName.contains("seekersTokenWin")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.seekersTokenWin, arenaInv.seekersTokenWin, 1000, 0, 1, 1);
                        } else if (displayName.contains("killTokens")) {
                            updownButton(player, item, arenaInv, Arena.ArenaType.killTokens, arenaInv.killTokens, 1000, 0, 1, 1);
                        }

                        InventoryHandler.openPanel(player, arenaInv.arenaName);
                    } else if (type.name().contains("CONCRETE")) {
                        if (displayName.contains("hidersPvp"))
                            booleanButton(player, item, arenaInv, Arena.ArenaType.hidersPVP);
                        else if (displayName.contains("seekerAttackSeeker"))
                            booleanButton(player, item, arenaInv, Arena.ArenaType.seekerAttackSeeker);
                    } else if (type.equals(Material.BOOK)) {
                        if (displayName.contains("disguiseBlocks")) {
                            InventoryHandler.openDisguiseBlocks(arenaInv, player);
                        }
                    }

                    save(arenaInv);
                }
            }
        }
    }

    public void save(Arena arena) {
        MemoryStorage.arenas.getFile().set(arena.arenaName, arena);
        MemoryStorage.arenas.save();
        ArenaHandler.loadArenas();
    }
}
