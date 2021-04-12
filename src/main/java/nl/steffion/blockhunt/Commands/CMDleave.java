package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.ArenaHandler;
import nl.steffion.blockhunt.ConfigC;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDleave extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            ArenaHandler.playerLeaveArena(player, true, true);
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
}
