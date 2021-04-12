package nl.steffion.blockhunt.Taunt;

import nl.steffion.blockhunt.BlockHunt;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.Random;

public class EntityBalloon implements TauntSupplier {
    private final EntityType entityType;
    private final String displayName;

    public EntityBalloon(EntityType entityBalloon, String displayName) {
        this.entityType = entityBalloon;
        this.displayName = displayName;
    }

    @Override
    public void accept(Player player) {
        World world = player.getWorld();
        Entity entity = world.spawnEntity(player.getLocation(), entityType);
        entity.setSilent(true);
        entity.setCustomName(displayName);
        entity.setInvulnerable(true);

        if (entity instanceof Creeper) {
            ((Creeper) entity).setExplosionRadius(0);
            ((Creeper) entity).setMaxFuseTicks(Integer.MAX_VALUE);
        }

        long start = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskTimer(BlockHunt.plugin, bukkitTask -> {
            if ((System.currentTimeMillis() - start) >= 3000) {
                spawnFirework(entity.getLocation());
                entity.remove();
                bukkitTask.cancel();
            }
            entity.setVelocity(new Vector(0, 0.1, 0));
        }, 0, 1);
    }

    public void spawnFirework(Location location) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Random random = new Random();
        int rt = random.nextInt(4) + 1;
        FireworkEffect.Type type = getFireworkType(rt);
        int u = random.nextInt(256);
        int b = random.nextInt(256);
        int g = random.nextInt(256);
        Color c1 = Color.fromRGB(u, g, b);
        u = random.nextInt(256);
        b = random.nextInt(256);
        g = random.nextInt(256);
        Color c2 = Color.fromRGB(u, g, b);
        FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(random.nextBoolean()).build();
        fwm.addEffect(effect);
        int rp = random.nextInt(2) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

    public FireworkEffect.Type getFireworkType(int i) {
        switch (i) {
            case 1:
                return FireworkEffect.Type.BURST;
            case 2:
                return FireworkEffect.Type.CREEPER;
            case 3:
                return FireworkEffect.Type.STAR;
            default:
                return FireworkEffect.Type.BALL;
        }
    }
}
