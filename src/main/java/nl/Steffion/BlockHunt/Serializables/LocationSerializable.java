package nl.Steffion.BlockHunt.Serializables;

import java.util.HashMap;
import java.util.Map;

import nl.Steffion.BlockHunt.Managers.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * No longer required as a location is already serializable
 */

@SerializableAs("BlockHuntLocation")
@Deprecated
public class LocationSerializable extends Location {
	public LocationSerializable(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	public LocationSerializable(Location loc) {
		super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LocationSerializable || o instanceof Location) {
			Location loc = (Location) o;
			return loc.getWorld().getName().equals(getWorld().getName()) && loc.getX() == getX() && loc.getY() == getY() && loc.getZ() == getZ()
					&& loc.getYaw() == getYaw() && loc.getPitch() == getPitch();
		}
		return false;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("world", getWorld().getName());
		map.put("x", getX());
		map.put("y", getY());
		map.put("z", getZ());
		if (getYaw() != 0D)
			map.put("yaw", getYaw());
		if (getPitch() != 0D)
			map.put("pitch", getPitch());
		return map;
	}

	public static LocationSerializable deserialize(Map<String, Object> map) {
		World w = Bukkit.getWorld((String)map.getOrDefault( "w", ""));
		if (w == null) {
			MessageManager.sendMessage(null, "%EError deserializing Location - world not found! (%A%w%%E)", "w-" + w);
			return null;
		}
		return new LocationSerializable(w, (Double) map.getOrDefault( "x", 0D), (Double) map.getOrDefault( "y", 0D), (Double) map.getOrDefault( "z", 0D), ((Double) map.getOrDefault( "a", 0D)).floatValue(),
				((Double) map.getOrDefault( "p", 0D)).floatValue());
	}

	public static Location getLocation(LocationSerializable ser){
		return new Location(ser.getWorld(),ser.getX(),ser.getY(),ser.getZ(),ser.getYaw(),ser.getPitch());
	}
}
