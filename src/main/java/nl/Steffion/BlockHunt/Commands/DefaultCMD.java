package nl.Steffion.BlockHunt.Commands;
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

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public abstract class DefaultCMD {


    public abstract boolean execute(Player player, Command cmd, String label, String[] args);
}
