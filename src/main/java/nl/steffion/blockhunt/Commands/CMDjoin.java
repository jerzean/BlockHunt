package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.ArenaHandler;
import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDjoin extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageManager.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + BlockHunt.CMDjoin.usage);
            } else {
                ArenaHandler.playerJoinArena(player, args[1]);
            }
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
}
