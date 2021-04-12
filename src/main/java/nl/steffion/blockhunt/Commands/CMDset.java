package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.InventoryHandler;
import nl.steffion.blockhunt.Managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDset extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageManager.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + BlockHunt.CMDset.usage);
            } else {
                String arenaname = args[1];
                InventoryHandler.openPanel(player, arenaname);
            }
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
}
