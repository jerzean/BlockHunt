package nl.Steffion.BlockHunt.Commands;
/**
 * Steffion's Engine - Made by Steffion.
 *
 * You're allowed to use this engine for own usage, you're not allowed to
 * republish the engine. Using this for your own plugin is allowed when a
 * credit is placed somewhere in the plugin.
 *
 * Thanks for your cooperate!
 *
 * @author Steffion
 */
import nl.Steffion.BlockHunt.Managers.MessageManager;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class DefaultCMD {


	public boolean exectue(Player player, Command cmd, String label, String[] args) {
		MessageManager.sendMessage(player, "%TAG%NExample of a Command!");
		// TODO Place the command stuff here.
		return true;
	}
}
