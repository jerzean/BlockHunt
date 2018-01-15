package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.W;
import nl.Steffion.BlockHunt.Managers.MessageManager;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDlist extends DefaultCMD {

	@Override
	public boolean exectue(Player player, Command cmd, String label, String[] args) {
		MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-" + BlockHunt.pdfFile.getName());
		if (W.arenaList.size() >= 1) {
			MessageManager.sendMessage(player, "&7Available arena(s):");
			for (Arena arena : W.arenaList) {
				MessageManager.sendMessage(player, "%A" + arena.arenaName);
			}
		} else {
			MessageManager.sendMessage(player, "&7&oNo arenas available...");
			MessageManager.sendMessage(player, "&7&oCreate an arena first please.");
		}
		MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oArenas list");
		return true;
	}
}
