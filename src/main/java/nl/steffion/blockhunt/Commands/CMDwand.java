package nl.steffion.blockhunt.Commands;

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.ConfigC;
import nl.steffion.blockhunt.MemoryStorage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CMDwand extends DefaultCMD {

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            ItemStack wand = new ItemStack(Material.getMaterial((String) MemoryStorage.config.get(ConfigC.wandIDname)));
            ItemMeta im = wand.getItemMeta();
            im.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.wandName)));
            MemoryStorage.config.load();
            List<String> lores = MemoryStorage.config.getFile().getStringList(ConfigC.wandDescription.location);
            List<String> lores2 = new ArrayList<>();
            for (String lore : lores) {
                lores2.add(MessageManager.replaceAll(lore));
            }

            im.setLore(lores2);
            wand.setItemMeta(im);
            player.getInventory().addItem(wand);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0);
            MessageManager.sendFMessage(player, ConfigC.normal_wandGaveWand, "type-" + wand.getType().toString().replaceAll("_", " ").toLowerCase());
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
}
