package nl.steffion.blockhunt.Managers;
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

import nl.steffion.blockhunt.Commands.DefaultCMD;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.MemoryStorage;
import nl.steffion.blockhunt.PermissionsC.Permissions;

public class CommandManager {


    public String name;
    public String label;
    public String args;
    public String argsalias;
    public Permissions permission;
    public ConfigC help;
    public boolean enabled;
    public DefaultCMD CMD;
    public String usage;

    public CommandManager(String name, String label, String args, String argsalias, Permissions permission, ConfigC help, boolean enabled, DefaultCMD CMD, String usage) {
        this.name = name;
        this.label = label;
        this.args = args;
        this.argsalias = argsalias;
        this.permission = permission;
        this.help = help;
        this.enabled = enabled;
        this.CMD = CMD;
        this.usage = usage;

        MemoryStorage.commands.add(this);
    }
}
