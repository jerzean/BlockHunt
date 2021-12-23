package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.Arena;
import nl.steffion.blockhunt.BlockHunt;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDremove extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageManager.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + BlockHunt.CMDremove.usage);
            } else {
                for (Arena arena : MemoryStorage.arenaList) {
                    if (args[1].equalsIgnoreCase(arena.arenaName)) {
                        MessageManager.sendFMessage(player, ConfigC.normal_removeRemovedArena, "name-" + args[1]);
                        MemoryStorage.arenas.getFile().set(args[1], null);
                        for (String sign : MemoryStorage.signs.getFile().getKeys(false)) {
                            if (MemoryStorage.signs.getFile().get(sign + ".arenaName").toString().equalsIgnoreCase(args[1])) {
                                Location signLoc = (Location) MemoryStorage.signs.getFile().get(sign + ".location");
                                signLoc.getBlock().setType(Material.AIR);
                                signLoc.getWorld().playEffect(signLoc, Effect.MOBSPAWNER_FLAMES, 0);
                                signLoc.getWorld().playSound(signLoc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
                                MemoryStorage.signs.getFile().set(sign, null);
                            }
                        }

                        MemoryStorage.arenas.save();
                        MemoryStorage.signs.load();

                        MemoryStorage.arenaList.remove(arena);
                        return true;
                    }
                }

                MessageManager.sendFMessage(player, ConfigC.error_noArena, "name-" + args[1]);
            }
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
}
