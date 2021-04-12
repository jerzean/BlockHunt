package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleportEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (MemoryStorage.pData.get(player) != null) {
            handlePlayer(event, player);
        }
    }

    /**
     * Handle a player that is in an arena
     *
     * @param event  PlayerTeleportEvent
     * @param player Player
     */
    private void handlePlayer(PlayerTeleportEvent event, Player player) {
        Location storedLoc = MemoryStorage.teleportLoc.remove(player);
        Location to = event.getTo();
        if (storedLoc == null || storedLoc.getWorld() != to.getWorld() || to.distanceSquared(storedLoc) > 1) {
            MessageManager.sendFMessage(player, ConfigC.error_teleportBlocked);
            event.setCancelled(true);
        }
    }
}
