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

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDinfo extends DefaultCMD {


    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-" + BlockHunt.pdfFile.getName());
        MessageManager.sendMessage(player, "%A%name%%N made by %A%authors%%N.", "name-" + BlockHunt.pdfFile.getName(), "authors-" + BlockHunt.pdfFile.getAuthors().get(0));
        MessageManager.sendMessage(player, "%NVersion: %A%version%%N.", "version-" + BlockHunt.pdfFile.getVersion());
        MessageManager.sendMessage(player, "%NType %A%helpusage% %Nfor help.", "helpusage-" + BlockHunt.CMDhelp.usage);
        MessageManager.sendMessage(player, "%NDev-Page: %Ahttp://dev.bukkit.org/bukkit-plugins/blockhunt/");
        MessageManager.sendMessage(player, "%NDonations are welcome!");
        MessageManager.sendMessage(player, "%NMade by help from some friends &c<3%N!");
        MessageManager.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oInfo Page");
        return true;
    }
}
