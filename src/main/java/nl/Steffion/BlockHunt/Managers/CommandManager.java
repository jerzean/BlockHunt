package nl.Steffion.BlockHunt.Managers;
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

import nl.Steffion.BlockHunt.Commands.DefaultCMD;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.MemoryStorage;
import nl.Steffion.BlockHunt.PermissionsC.Permissions;

import java.util.List;

public class CommandManager {


    public String name;
    public String label;
    public String args;
    public String argsalias;
    public Permissions permission;
    public ConfigC help;
    public boolean enabled;
    public List<String> mainTABlist;
    public DefaultCMD CMD;
    public String usage;

    public CommandManager(String name, String label, String args, String argsalias, Permissions permission, ConfigC help, Boolean enabled, List<String> mainTABlist,
                          DefaultCMD CMD, String usage) {
        this.name = name;
        this.label = label;
        this.args = args;
        this.argsalias = argsalias;
        this.permission = permission;
        this.help = help;
        this.enabled = enabled;
        this.mainTABlist = mainTABlist;
        this.CMD = CMD;
        this.usage = usage;

        MemoryStorage.commands.add(this);
    }
}
