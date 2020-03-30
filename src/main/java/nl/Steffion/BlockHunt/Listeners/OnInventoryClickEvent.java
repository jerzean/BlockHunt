package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Arena.ArenaType;
import nl.Steffion.BlockHunt.MemoryStorage;
import nl.Steffion.BlockHunt.Managers.MessageManager;

import org.bukkit.Bukkit;
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

public class OnInventoryClickEvent implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		for (Arena arena : MemoryStorage.arenaList) {
			if (arena.playersInArena.contains(player) && !arena.seekers.contains(player)) {
				if (event.getSlot() == 8 || event.getSlot() == 39) {
					event.setCancelled(true);
				}
			}
		}

		Inventory inv = event.getInventory();
		InventoryView invView = event.getView();
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
					if (event.getCurrentItem().getType() != Material.AIR) {
						if (event.getCurrentItem().getType().isBlock()) {
							MemoryStorage.choosenBlock.put(player, event.getCurrentItem());
							MessageManager.sendFMessage(player, ConfigC.normal_shopChoosenBlock, "block-"
									+ event.getCurrentItem().getType().toString().replaceAll("_", "").replaceAll("BLOCK", "").toLowerCase());
						} else {
							MessageManager.sendFMessage(player, ConfigC.error_setNotABlock);
						}
					}
				} else if (invView.getTitle().contains(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name)))) {
					event.setCancelled(true);
					if (event.getCurrentItem().getType() != Material.AIR) {
						if (event.getCurrentItem().getType().equals(Material.BLUE_WOOL)) {
							int i = 0;
							for (Arena arena : MemoryStorage.arenaList) {
								if (arena.playersInArena.contains(player)) {
									for (Player playerCheck : arena.playersInArena) {
										if (MemoryStorage.choosenSeeker.get(playerCheck) != null) {
											if (MemoryStorage.choosenSeeker.get(playerCheck)) {
												i = i + 1;
											}
										}
									}
								}

								if (i >= arena.amountSeekersOnStart) {
									MessageManager.sendFMessage(player, ConfigC.error_shopMaxSeekersReached);
								} else {
									MemoryStorage.choosenSeeker.put(player, true);
									player.getInventory().setItemInHand(new ItemStack(Material.AIR));
									player.updateInventory();
									MessageManager.sendFMessage(player, ConfigC.normal_shopChoosenSeeker);
									inv.clear();
									if (MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") == 1) {
										MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", null);
										MemoryStorage.shop.save();
									} else {
										MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") - 1);
										MemoryStorage.shop.save();
									}
								}
							}

						} else if (event.getCurrentItem().getType().equals(Material.RED_WOOL)) {
							int i = 0;
							for (Arena arena : MemoryStorage.arenaList) {
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
									player.getInventory().setItemInHand(new ItemStack(Material.AIR));
									player.updateInventory();
									MessageManager.sendFMessage(player, ConfigC.normal_shopChoosenHiders);
									inv.clear();
									if (MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") == 1) {
										MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", null);
										MemoryStorage.shop.save();
									} else {
										MemoryStorage.shop.getFile().set(player.getName() + ".blockhuntpass", MemoryStorage.shop.getFile().getInt(player.getName() + ".blockhuntpass") - 1);
										MemoryStorage.shop.save();
									}
								}
							}
						}
					}
				} else {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					String arenaname = inv.getItem(0).getItemMeta().getDisplayName().replaceAll(MessageManager.replaceAll("%NBlockHunt arena: %A"), "");

					Arena arena = null;
					for (Arena arena2 : MemoryStorage.arenaList) {
						if (arena2.arenaName.equalsIgnoreCase(arenaname)) {
							arena = arena2;
						}
					}

					if (item == null)
						return;
					if (item.getType().equals(Material.AIR))
						return;
					if (!item.getItemMeta().hasDisplayName())
						return;
					if (item.getType().equals(Material.GOLD_NUGGET)) {
						if (item.getItemMeta().getDisplayName().contains("maxPlayers")) {
							updownButton(player, item, arena, ArenaType.maxPlayers, arena.maxPlayers, Bukkit.getMaxPlayers(), 2, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("minPlayers")) {
							updownButton(player, item, arena, ArenaType.minPlayers, arena.minPlayers, Bukkit.getMaxPlayers() - 1, 2, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("amountSeekersOnStart")) {
							updownButton(player, item, arena, ArenaType.amountSeekersOnStart, arena.amountSeekersOnStart, arena.maxPlayers - 1, 1, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("timeInLobbyUntilStart")) {
							updownButton(player, item, arena, ArenaType.timeInLobbyUntilStart, arena.timeInLobbyUntilStart, 1000, 5, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("waitingTimeSeeker")) {
							updownButton(player, item, arena, ArenaType.waitingTimeSeeker, arena.waitingTimeSeeker, 1000, 5, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("gameTime")) {
							updownButton(player, item, arena, ArenaType.gameTime, arena.gameTime, 1000, 5, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("blockAnnouncerTime")) {
							updownButton(player, item, arena, ArenaType.blockAnnouncerTime, arena.blockAnnouncerTime, 1000, 0, 5, 5);
						} else if (item.getItemMeta().getDisplayName().contains("timeUntilHidersSword")) {
							updownButton(player, item, arena, ArenaType.timeUntilHidersSword, arena.timeUntilHidersSword, 1000, 0, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("hidersTokenWin")) {
							updownButton(player, item, arena, ArenaType.hidersTokenWin, arena.hidersTokenWin, 1000, 0, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("seekersTokenWin")) {
							updownButton(player, item, arena, ArenaType.seekersTokenWin, arena.seekersTokenWin, 1000, 0, 1, 1);
						} else if (item.getItemMeta().getDisplayName().contains("killTokens")) {
							updownButton(player, item, arena, ArenaType.killTokens, arena.killTokens, 1000, 0, 1, 1);
						}

						save(arena);
						InventoryHandler.openPanel(player, arena.arenaName);

					} else if (item.getType().equals(Material.BOOK)) {
						if (item.getItemMeta().getDisplayName().contains("disguiseBlocks")) {
							InventoryHandler.openDisguiseBlocks(arena, player);
						}
					}
				}
			}
		}
	}

	public void save(Arena arena) {
		MemoryStorage.arenas.getFile().set(arena.arenaName, arena);
		MemoryStorage.arenas.save();
		ArenaHandler.loadArenas();
	}

	public static void updownButton(Player player, ItemStack item, Arena arena, ArenaType at, int option, int max, int min, int add, int remove) {
		if (item.getItemMeta().getDisplayName().contains((String) MemoryStorage.messages.get(ConfigC.button_add2))) {
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
		} else if (item.getItemMeta().getDisplayName().contains((String) MemoryStorage.messages.get(ConfigC.button_remove2))) {
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
}
