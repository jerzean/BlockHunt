package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.ArenaHandler;
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
        Arena arena = ArenaHandler.getArenaByPlayer(player);

        if (arena != null)
            event.setCancelled(true);
    }
}
