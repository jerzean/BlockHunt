package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.InventoryHandler;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDshop extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        InventoryHandler.openShop(player);
        return true;
    }
}
