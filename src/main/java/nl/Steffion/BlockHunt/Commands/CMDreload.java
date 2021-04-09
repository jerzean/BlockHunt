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

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.ConfigManager;
import nl.Steffion.BlockHunt.Managers.MessageManager;
import nl.Steffion.BlockHunt.MemoryStorage;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDreload extends DefaultCMD {


    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        ConfigManager.newFiles();

        MemoryStorage.config.load();
        MemoryStorage.messages.load();
        MemoryStorage.arenas.load();
        MemoryStorage.signs.load();
        MemoryStorage.shop.load();
        for (Arena arena : MemoryStorage.arenaList) {
            String cause = "[BlockHunt] Arena " + arena.arenaName + " has been stopped";
            ArenaHandler.stopArena(arena, cause, ConfigC.warning_arenaStopped);
        }

        ArenaHandler.loadArenas();
        MessageManager.sendFMessage(player, ConfigC.normal_reloadedConfigs);
        return true;
    }
}
