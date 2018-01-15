package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageManager;

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
