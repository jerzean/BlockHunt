package nl.steffion.blockhunt.Managers;

import com.google.common.collect.ImmutableList;
import nl.steffion.blockhunt.MemoryStorage;
import nl.steffion.blockhunt.Taunt.Taunt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TauntManager {

    private static final List<Taunt> tauntList = new ArrayList<>();

    public static boolean isTauntItem(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        return tauntList.stream().anyMatch(taunt -> taunt.itemStack.isSimilar(itemStack));
    }

    public static Taunt getTaunt(ItemStack stack) {
        if (stack == null)
            return null;
        return tauntList.stream().filter(taunt -> taunt.itemStack.isSimilar(stack)).findFirst().orElse(null);
    }

    public static void register(Taunt taunt) {
        if (isTauntItem(taunt.itemStack))
            return;

        tauntList.add(taunt);
    }

    public static List<Taunt> getTauntList() {
        return ImmutableList.copyOf(tauntList);
    }

    public static boolean canTaunt(Player player) {
        if (!MemoryStorage.hiddenLoc.containsKey(player))
            return false;
        if (MemoryStorage.lastTauntUsed.containsKey(player)) {
            long lastTaunt = getRemainingTime(player);
            return lastTaunt >= System.currentTimeMillis();
        } else return true;
    }

    public static long getRemainingTime(Player player) {
        if (MemoryStorage.lastTauntUsed.containsKey(player)) {
            return MemoryStorage.lastTauntUsed.get(player) - System.currentTimeMillis();
        }
        return 0L;
    }
}
