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

import nl.steffion.blockhunt.Managers.CommandManager;
import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.Managers.PermissionsManager;
import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDhelp extends DefaultCMD {


    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        int amountCommands = 0;
        for (CommandManager command : MemoryStorage.commands) {
            if (command.usage != null) {
                amountCommands = amountCommands + 1;
            }
        }

        int maxPages = Math.round(amountCommands / 3);
        if (maxPages <= 0) {
            maxPages = 1;
        }

        if (args.length == 1) {
            int page = 1;
            MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-" + BlockHunt.pdfFile.getName() + " %Nhelp page %A" + page + "%N/%A" + maxPages);
            int i = 1;
            for (CommandManager command : MemoryStorage.commands) {
                if (i <= 4) {
                    if (command.usage != null) {
                        if (PermissionsManager.hasPerm(player, command.permission, false)) {
                            MessageManager.sendMessage(player, "%A" + command.usage + "%N - " + MemoryStorage.messages.getFile().get(command.help.location));
                        } else {
                            MessageManager.sendMessage(player, "%MemoryStorage" + command.usage + "%N - " + MemoryStorage.messages.getFile().get(command.help.location));
                        }
                        i = i + 1;
                    }
                }
            }

            MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oHelp Page");
        } else {
            int page = 1;
            try {
                page = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
                page = 1;
            }

            if (maxPages < page) {
                maxPages = page;
            }

            MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-" + BlockHunt.pdfFile.getName() + " %Nhelp page %A" + page + "%N/%A" + maxPages);

            int i = 1;
            for (CommandManager command : MemoryStorage.commands) {
                if (i <= (page * 4) + 4) {
                    if (command.usage != null) {
                        if (i >= ((page - 1) * 4) + 1 && i <= ((page - 1) * 4) + 4) {
                            if (PermissionsManager.hasPerm(player, command.permission, false)) {
                                MessageManager.sendMessage(player, "%A" + command.usage + "%N - " + MemoryStorage.messages.getFile().get(command.help.location));
                            } else {
                                MessageManager.sendMessage(player, "%MemoryStorage" + command.usage + "%N - " + MemoryStorage.messages.getFile().get(command.help.location));
                            }
                        }
                        i = i + 1;
                    }
                }
            }
            MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oHelp Page");
        }
        return true;
    }
}
