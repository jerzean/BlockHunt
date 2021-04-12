package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.ArenaHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class OnFoodLevelChangeEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        HumanEntity humanEntity = event.getEntity();

        if (humanEntity instanceof Player) {
            Player player = (Player) humanEntity;
            Arena arena = ArenaHandler.getArenaByPlayer(player);
            player.setSaturation(0);

            if (arena != null)
                event.setCancelled(true);
        }
    }
}
