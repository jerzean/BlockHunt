package nl.Steffion.BlockHunt;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.FallingBlockWatcher;
import nl.Steffion.BlockHunt.Managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SolidBlockHandler {

    public static void makePlayerUnsolid(Player player) {
        Arena arena = MemoryStorage.arenaList.stream().filter(arena1 -> arena1.playersInArena.contains(player)).findFirst().orElse(null);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "Arena is null !");
            System.out.println("[BlockHunt] ERROR: " + player.getName() + " Arena is null !");
            return;
        }

        ItemStack itemBlock = player.getInventory().getItem(8);

        if (itemBlock == null) {
            player.sendMessage(ChatColor.RED + "Unable to hide you because your inventory block is missing!");
            System.out.println("[BlockHunt] ERROR: " + player.getName() + " could not be hidden because their inventory block was missing!");
            return;
        }

        Block pBlock = null;

        if (MemoryStorage.hiddenLoc.get(player) != null) {
            pBlock = MemoryStorage.hiddenLoc.get(player).getBlock();
        }

        itemBlock.setAmount(5);

        boolean isWater = MemoryStorage.hiddenLocWater.getOrDefault(player, false);

        if (pBlock != null) {
            Block finalPBlock = pBlock;

            arena.playersInArena.forEach(arenaPlayer -> {
                if (!player.equals(arenaPlayer)) {
                    if (isWater) {
                        arenaPlayer.sendBlockChange(finalPBlock.getLocation(), Bukkit.createBlockData(Material.WATER));
                    } else {
                        arenaPlayer.sendBlockChange(finalPBlock.getLocation(), Bukkit.createBlockData(Material.AIR));
                    }

                    arenaPlayer.showPlayer(BlockHunt.plugin, player);
                }
            });
        }

        MemoryStorage.hiddenLocWater.remove(player);
        MemoryStorage.hiddenLoc.remove(player);

        player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
        itemBlock.removeEnchantment(Enchantment.DURABILITY);

        MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, itemBlock.getType());
        ((FallingBlockWatcher) disguise.getWatcher()).setGridLocked(false);
        disguise.getWatcher().setNoGravity(true);
        DisguiseAPI.disguiseToPlayers(player, disguise, arena.playersInArena);
        MessageManager.sendFMessage(player, ConfigC.normal_ingameNoMoreSolid);
    }
}
