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
import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.W;
import nl.Steffion.BlockHunt.Managers.ConfigManager;
import nl.Steffion.BlockHunt.Managers.MessageManager;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDreload extends DefaultCMD {


	@Override
	public boolean exectue(Player player, Command cmd, String label, String[] args) {
		ConfigManager.newFiles();

		W.config.load();
		W.messages.load();
		W.arenas.load();
		W.signs.load();
		W.shop.load();
		for (Arena arena : W.arenaList) {
			ArenaHandler.stopArena(arena);
		}

		ArenaHandler.loadArenas();
		MessageManager.sendFMessage(player, ConfigC.normal_reloadedConfigs);
		return true;
	}
}
