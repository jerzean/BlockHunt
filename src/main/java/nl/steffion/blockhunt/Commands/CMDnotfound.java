package nl.steffion.blockhunt.Commands;
/**
 * Steffion's Engine - Made by Steffion.
 * <p>
 * You're allowed to use this engine for own usage, you're not allowed to
 * republish the engine. Using this for your own plugin is allowed when a
 * credit is placed somewhere in the plugin.
 * <p>
 * Thanks for your cooperate!
 *
 * @author Steffion
 */

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.ConfigC;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDnotfound extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        MessageManager.sendFMessage(player, ConfigC.error_commandNotFound);
        return true;
    }
}
