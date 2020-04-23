package nl.Steffion.BlockHunt;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerHandler {
	public static void teleport(Player player, Location location) {
		MemoryStorage.teleportLoc.put(player, location);
		player.teleport(location);
	}
}
