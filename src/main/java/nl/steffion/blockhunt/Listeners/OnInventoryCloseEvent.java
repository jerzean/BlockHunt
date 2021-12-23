package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.ArenaHandler;
import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class OnInventoryCloseEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryView invView = event.getView();
        if (inv.getType().equals(InventoryType.CHEST)) {
            if (invView.getTitle().contains("DisguiseBlocks")) {
                String arenaname = inv.getItem(0).getItemMeta().getDisplayName().replaceAll(MessageManager.replaceAll("%NDisguiseBlocks of arena: %A"), "");

                Arena arena = null;
                for (Arena arena2 : MemoryStorage.arenaList) {
                    if (arena2.arenaName.equalsIgnoreCase(arenaname)) {
                        arena = arena2;
                    }
                }

                ArrayList<ItemStack> blocks = new ArrayList<>();

                for (ItemStack item : inv.getContents()) {
                    if (item != null) {
                        if (!item.getType().equals(Material.PAPER)) {
                            if (item.getType().equals(Material.FLOWER_POT)) {
                                blocks.add(new ItemStack(Material.FLOWER_POT));
                            } else {
                                blocks.add(item);
                            }
                        }
                    }
                }

                arena.disguiseBlocks = blocks;
                save(arena);
            }
        }
    }

    public void save(Arena arena) {
        MemoryStorage.arenas.getFile().set(arena.arenaName, arena);
        MemoryStorage.arenas.save();
        ArenaHandler.loadArenas();
    }
}
