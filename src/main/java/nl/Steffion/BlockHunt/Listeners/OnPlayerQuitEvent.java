package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Arena arena = ArenaHandler.getArenaByPlayer(player);

        if (arena != null)
            ArenaHandler.playerLeaveArena(player, true, true);

        //Disguise.unDisguise(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Disguise.disguise(event.getPlayer(), new ItemStack(Material.STONE));
    }

//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onPlayerJoinEvent(PlayerJoinEvent event) {
//		Player playerJoining = event.getPlayer();
//		playerJoining.teleport(playerJoining.getWorld().getSpawnLocation());
//	}
}
