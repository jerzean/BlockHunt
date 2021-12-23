package nl.steffion.blockhunt.Taunt;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Random;

public class SplashPotionTaunt implements TauntSupplier {
    private static final Random random = new Random();

    @Override
    public void accept(Player player) {
        World world = player.getWorld();
        Location playerLoc = player.getLocation();
        for (int i = 0; i <= 6; i++) {
            double x = random(playerLoc.getX());
            double y = random(playerLoc.getY());
            double z = random(playerLoc.getZ());
            Location location = new Location(world, x, y, z).add(0, 1, 0);
            world.spawnEntity(location, EntityType.SPLASH_POTION);
        }
    }

    public double random(double midle) {
        boolean bool = random.nextBoolean();
        return midle + (bool ? -random.nextDouble() : random.nextDouble());
    }
}
