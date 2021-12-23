package nl.steffion.blockhunt.Listeners;

import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.Managers.PermissionsManager;
import nl.steffion.blockhunt.PermissionsC;
import nl.steffion.blockhunt.SignsHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChangeEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSignChangeEvent(SignChangeEvent event) {
        Player player = event.getPlayer();
        String[] lines = event.getLines();
        if (lines[0] != null) {
            if (lines[0].equalsIgnoreCase("[" + BlockHunt.pdfFile.getName() + "]")) {
                if (PermissionsManager.hasPerm(player, PermissionsC.Permissions.signcreate, true)) {
                    SignsHandler.createSign(event, lines, event.getBlock().getLocation());
                }
            }
        }
    }
}
