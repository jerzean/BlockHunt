package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.ArenaHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class OnPlayerDropItemEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Arena arena = ArenaHandler.getArenaByPlayer(player);

        if (arena != null)
            event.setCancelled(true);
    }
}
