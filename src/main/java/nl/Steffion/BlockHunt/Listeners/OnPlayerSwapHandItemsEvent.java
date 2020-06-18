package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.MemoryStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class OnPlayerSwapHandItemsEvent implements Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        // Early exit if no one is in any arena
        if (ArenaHandler.noPlayersInArenas()) return;

        Player player = event.getPlayer();
        for (Arena arena : MemoryStorage.arenaList) {
            if (arena.playersInArena.contains(player)) {
                event.setCancelled(true);
            }
        }
    }
}
