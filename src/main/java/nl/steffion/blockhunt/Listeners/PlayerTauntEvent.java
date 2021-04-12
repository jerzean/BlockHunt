package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.ArenaHandler;
import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.Managers.TauntManager;
import nl.steffion.blockhunt.MemoryStorage;
import nl.steffion.blockhunt.Taunt.Taunt;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class PlayerTauntEvent implements Listener {

    public static final String DISPLAY_NAME = ChatColor.GREEN + "" + ChatColor.BOLD + "Taunts";

    private static final SimpleDateFormat formater = new SimpleDateFormat("ss:SS");

    @EventHandler(ignoreCancelled = true)
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Arena arena = ArenaHandler.getArenaByPlayer(player);

        if (arena != null && arena.gameState == Arena.ArenaState.STARTED) {
            ItemStack stackInHand = player.getInventory().getItemInMainHand();
            if (stackInHand.getItemMeta() != null && stackInHand.getItemMeta().hasDisplayName() && stackInHand.getItemMeta().getDisplayName().equalsIgnoreCase(DISPLAY_NAME)) {
                boolean cantTaunt = TauntManager.canTaunt(player);
                if (cantTaunt) {
                    openInv(player);
                } else {
                    long remainingTime = TauntManager.getRemainingTime(player);
                    player.sendMessage(ChatColor.RED + "Please wait " + ChatColor.GOLD + formater.format(new Date(remainingTime)) + ChatColor.RED + "s for using taunt !");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, SoundCategory.PLAYERS, 2, 0.7F);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Arena arena = ArenaHandler.getArenaByPlayer(player);
            Inventory clickedInv = event.getClickedInventory();
            InventoryView view = event.getView();

            if (clickedInv != null && arena != null && view.getTitle().equalsIgnoreCase(DISPLAY_NAME) && arena.gameState == Arena.ArenaState.STARTED) {
                ItemStack clickItem = event.getCurrentItem();
                if (TauntManager.isTauntItem(clickItem)) {
                    Taunt taunt = TauntManager.getTaunt(clickItem);
                    if (taunt != null) {
                        player.closeInventory();
                        taunt.tauntSupplier.accept(player);
                        MemoryStorage.lastTauntUsed.put(player, taunt.nextAllowedTaunt());
                        Bukkit.getScheduler().runTaskLater(BlockHunt.plugin, () -> MemoryStorage.lastTauntUsed.remove(player), taunt.toSecondDelay() * 20L);
                    } else {
                        player.sendMessage("Error, detect a taunt item but cannot execute this !?");
                    }
                }
            }
        }
    }

    public void openInv(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, DISPLAY_NAME);

        ItemStack stack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("");
        stack.setItemMeta(meta);

        Iterator<Taunt> tauntIterator = TauntManager.getTauntList().listIterator();

        for (int i = 0; i < 54; i++) {
            if (i >= 10 && i <= 16)
                inventory.setItem(i, getTautStack(tauntIterator));
            else if (i >= 19 && i <= 25)
                inventory.setItem(i, getTautStack(tauntIterator));
            else if (i >= 28 && i <= 34)
                inventory.setItem(i, getTautStack(tauntIterator));
            else if (i >= 37 && i <= 43)
                inventory.setItem(i, getTautStack(tauntIterator));
            else inventory.setItem(i, stack);
        }
        player.openInventory(inventory);
    }

    public ItemStack getTautStack(Iterator<Taunt> tauntIterator) {
        return tauntIterator.hasNext() ? tauntIterator.next().itemStack : new ItemStack(Material.AIR);
    }
}
