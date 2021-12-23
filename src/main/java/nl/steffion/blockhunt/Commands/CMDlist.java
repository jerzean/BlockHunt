package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDlist extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-" + BlockHunt.pdfFile.getName());
        if (MemoryStorage.arenaList.size() >= 1) {
            MessageManager.sendMessage(player, "&7Available arena(s):");
            for (Arena arena : MemoryStorage.arenaList) {
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
