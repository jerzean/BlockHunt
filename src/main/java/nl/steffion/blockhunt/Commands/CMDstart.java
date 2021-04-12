package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDstart extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageManager.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + BlockHunt.CMDstart.usage);
            } else {
                Arena arena = null;
                for (Arena arena2 : MemoryStorage.arenaList) {
                    if (arena2.arenaName.equalsIgnoreCase(args[1])) {
                        arena = arena2;
                    }
                }

                if (arena != null) {
                    if (arena.gameState.equals(Arena.ArenaState.WAITING)) {
                        if (arena.playersInArena.size() >= 2) {
                            arena.timer = 11;
                            arena.gameState = Arena.ArenaState.STARTING;
                            MessageManager.sendFMessage(player, ConfigC.normal_startForced, "arenaname-" + arena.arenaName);
                        } else {
                            MessageManager.sendFMessage(player, ConfigC.warning_lobbyNeedAtleast, "1-2");
                        }
                    } else if (arena.gameState.equals(Arena.ArenaState.STARTING)) {
                        if (arena.playersInArena.size() < arena.maxPlayers) {
                            if (arena.timer >= 10) {
                                arena.timer = 11;
                            }
                        } else {
                            arena.timer = 1;
                        }

                        MessageManager.sendFMessage(player, ConfigC.normal_startForced, "arenaname-" + arena.arenaName);
                    }
                } else {
                    MessageManager.sendFMessage(player, ConfigC.error_noArena, "name-" + args[1]);
                }
            }
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
}
