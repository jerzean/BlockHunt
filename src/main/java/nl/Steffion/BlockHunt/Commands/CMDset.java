package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.InventoryHandler;
import nl.Steffion.BlockHunt.Managers.MessageManager;

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
