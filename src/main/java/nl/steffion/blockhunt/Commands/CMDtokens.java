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

import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDtokens extends DefaultCMD {


    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (args.length <= 3) {
            MessageManager.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + BlockHunt.CMDtokens.usage);
        } else {
            String option = args[1];
            String playerName = args[2];
            int amount = 0;
            try {
                amount = Integer.valueOf(args[3]);
            } catch (NumberFormatException e) {
                MessageManager.sendFMessage(player, ConfigC.error_notANumber, "1-" + args[3]);
                return true;
            }

            Player tokenPlayer = Bukkit.getPlayer(playerName);
            if (tokenPlayer == null) {
                MessageManager.sendFMessage(player, ConfigC.error_tokensPlayerNotOnline, "playername-" + playerName);
                return true;
            }
            String name = "\u00A78Console";
            if (player != null) {
                name = player.getName();
            }

            if (option.equalsIgnoreCase("set")) {
                MemoryStorage.shop.getFile().set(tokenPlayer.getName() + ".tokens", amount);
                MemoryStorage.shop.save();
                MessageManager.sendFMessage(player, ConfigC.normal_tokensChanged, "option-Set", "playername-" + tokenPlayer.getName(), "option2-to", "amount-" + amount);
                MessageManager.sendFMessage(tokenPlayer, ConfigC.normal_tokensChangedPerson, "option-set", "playername-" + name, "option2-to", "amount-" + amount);
            } else if (option.equalsIgnoreCase("add")) {
                int tokens = 0;
                if (MemoryStorage.shop.getFile().getInt(tokenPlayer.getName() + ".tokens") != 0) {
                    tokens = MemoryStorage.shop.getFile().getInt(tokenPlayer.getName() + ".tokens");
                }
                MemoryStorage.shop.getFile().set(tokenPlayer.getName() + ".tokens", tokens + amount);
                MemoryStorage.shop.save();
                MessageManager.sendFMessage(player, ConfigC.normal_tokensChanged, "option-Added", "playername-" + tokenPlayer.getName(), "option2-to", "amount-" + amount);
                MessageManager.sendFMessage(tokenPlayer, ConfigC.normal_tokensChangedPerson, "option-added", "playername-" + name, "option2-to", "amount-" + amount);
            } else if (option.equalsIgnoreCase("take")) {
                int tokens = 0;
                if (MemoryStorage.shop.getFile().getInt(tokenPlayer.getName() + ".tokens") != 0) {
                    tokens = MemoryStorage.shop.getFile().getInt(tokenPlayer.getName() + ".tokens");
                }
                MemoryStorage.shop.getFile().set(tokenPlayer.getName() + ".tokens", tokens - amount);
                MemoryStorage.shop.save();
                MessageManager.sendFMessage(player, ConfigC.normal_tokensChanged, "option-Took", "playername-" + tokenPlayer.getName(), "option2-from", "amount-" + amount);
                MessageManager.sendFMessage(tokenPlayer, ConfigC.normal_tokensChangedPerson, "option-took", "playername-" + name, "option2-from", "amount-" + amount);
            } else {
                MessageManager.sendFMessage(player, ConfigC.error_tokensUnknownsetting, "option-" + option);
            }
        }
        return true;
    }
}
