package nl.steffion.blockhunt.Disguise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Disguise implements Listener {

    private static final HashMap<Player, Entity> targetToDisguise = new HashMap<>();

    public static void disguise(Player target, List<Player> viewers) {
        if (targetToDisguise.containsKey(target))
            unDisguise(target);

        World world = target.getWorld();
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(target.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setInvulnerable(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setVisible(false);

        ItemStack stack = target.getInventory().getItem(8);

        if (stack == null)
            return;

        FallingBlock fallingBlock = world.spawnFallingBlock(target.getLocation(), stack.getType().createBlockData());
        armorStand.addPassenger(fallingBlock);

        for (Player view : viewers)
            sendRemoveEntity(view, armorStand, fallingBlock);

        targetToDisguise.put(target, fallingBlock);
    }

    public static void disguise(Player target, Player... viewers) {
        disguise(target, Arrays.asList(viewers));
    }

    public static void unDisguise(Player entity) {
        targetToDisguise.remove(entity);
    }

    private static void sendRemoveEntity(Player player, Entity... toKill) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        container.getIntegers().write(0, toKill.length);
        container.getIntegerArrays().write(0, Stream.of(toKill).mapToInt(Entity::getEntityId).toArray());
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Player, Entity> getTargetToDisguise() {
        return targetToDisguise;
    }
}
