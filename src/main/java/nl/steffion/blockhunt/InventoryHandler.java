package nl.steffion.blockhunt;

import nl.steffion.blockhunt.Managers.MessageManager;
import nl.steffion.blockhunt.Managers.PermissionsManager;
import nl.steffion.blockhunt.Arena.ArenaType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryHandler {

    public static void openPanel(Player player, String arenaname) {

        Arena arena = null;
        for (Arena arena2 : MemoryStorage.arenaList) {
            if (arena2.arenaName.equalsIgnoreCase(arenaname)) {
                arena = arena2;
            }
        }

        if (arena != null) {
            String shorten = arena.arenaName;
            arenaname = arena.arenaName;
            if (shorten.length() > 6)
                shorten = shorten.substring(0, 6);
            Inventory panel = Bukkit.createInventory(null, 54, MessageManager.replaceAll("\u00A7r%N&lBlockHunt Arena: %A" + shorten));

            ItemStack arenaNameNote = new ItemStack(Material.PAPER, 1);
            ItemMeta arenaNameNote_IM = arenaNameNote.getItemMeta();
            arenaNameNote_IM.setDisplayName(MessageManager.replaceAll("%NBlockHunt arena: %A" + arena.arenaName));
            arenaNameNote.setItemMeta(arenaNameNote_IM);
            panel.setItem(0, arenaNameNote);

            //

            ItemStack maxPlayers_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack maxPlayers = new ItemStack(Material.BEDROCK, arena.maxPlayers);
            ItemStack maxPlayers_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack minPlayers_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack minPlayers = new ItemStack(Material.BEDROCK, arena.minPlayers);
            ItemStack minPlayers_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack amountSeekersOnStart_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack amountSeekersOnStart = new ItemStack(Material.BEDROCK, arena.amountSeekersOnStart);
            ItemStack amountSeekersOnStart_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack timeInLobbyUntilStart_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack timeInLobbyUntilStart = new ItemStack(Material.BEDROCK, arena.timeInLobbyUntilStart);
            ItemStack timeInLobbyUntilStart_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack waitingTimeSeeker_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack waitingTimeSeeker = new ItemStack(Material.BEDROCK, arena.waitingTimeSeeker);
            ItemStack waitingTimeSeeker_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack gameTime_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack gameTime = new ItemStack(Material.BEDROCK, arena.gameTime);
            ItemStack gameTime_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack disguiseBlocks_NOTE = new ItemStack(Material.BOOK, 1);
            ItemMeta disguiseBlocks_NOTE_IM = disguiseBlocks_NOTE.getItemMeta();
            disguiseBlocks_NOTE_IM.setDisplayName(MessageManager.replaceAll("%NSet the %AdisguiseBlocks%N."));
            disguiseBlocks_NOTE.setItemMeta(disguiseBlocks_NOTE_IM);
            panel.setItem(36, disguiseBlocks_NOTE);

            ItemStack blockAnnouncerTime_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack blockAnnouncerTime = new ItemStack(Material.BEDROCK, arena.blockAnnouncerTime);
            ItemStack blockAnnouncerTime_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack timeUntilHidersSword_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack timeUntilHidersSword = new ItemStack(Material.BEDROCK, arena.timeUntilHidersSword);
            ItemStack timeUntilHidersSword_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack hidersTokenWin_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack hidersTokenWin = new ItemStack(Material.BEDROCK, arena.hidersTokenWin);
            ItemStack hidersTokenWin_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack seekersTokenWin_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack seekersTokenWin = new ItemStack(Material.BEDROCK, arena.seekersTokenWin);
            ItemStack seekersTokenWin_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            ItemStack killTokens_UP = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemStack killTokens = new ItemStack(Material.BEDROCK, arena.killTokens);
            ItemStack killTokens_DOWN = new ItemStack(Material.GOLD_NUGGET, 1);

            //

            updownButton(panel, arena, ArenaType.maxPlayers, "maxPlayers", "1", maxPlayers_UP, maxPlayers, maxPlayers_DOWN, 1, 10, 19);
            updownButton(panel, arena, ArenaType.minPlayers, "minPlayers", "1", minPlayers_UP, minPlayers, minPlayers_DOWN, 2, 11, 20);

            updownButton(panel, arena, ArenaType.amountSeekersOnStart, "amountSeekersOnStart", "1", amountSeekersOnStart_UP, amountSeekersOnStart,
                    amountSeekersOnStart_DOWN, 4, 13, 22);

            updownButton(panel, arena, ArenaType.timeInLobbyUntilStart, "timeInLobbyUntilStart", "5 %Nsecond", timeInLobbyUntilStart_UP, timeInLobbyUntilStart,
                    timeInLobbyUntilStart_DOWN, 6, 15, 24);

            updownButton(panel, arena, ArenaType.waitingTimeSeeker, "waitingTimeSeeker", "5 %Nsecond", waitingTimeSeeker_UP, waitingTimeSeeker, waitingTimeSeeker_DOWN,
                    7, 16, 25);

            updownButton(panel, arena, ArenaType.gameTime, "gameTime", "5 %Nsecond", gameTime_UP, gameTime, gameTime_DOWN, 8, 17, 26);

            updownButton(panel, arena, ArenaType.blockAnnouncerTime, "blockAnnouncerTime", "5 %Nseconds", blockAnnouncerTime_UP, blockAnnouncerTime, blockAnnouncerTime_DOWN, 31, 40, 49);
            updownButton(panel, arena, ArenaType.timeUntilHidersSword, "timeUntilHidersSword", "5 %Nsecond", timeUntilHidersSword_UP, timeUntilHidersSword, timeUntilHidersSword_DOWN, 32, 41, 50);
            updownButton(panel, arena, ArenaType.hidersTokenWin, "hidersTokenWin", "1 %Ntoken", hidersTokenWin_UP, hidersTokenWin, hidersTokenWin_DOWN, 33, 42, 51);
            updownButton(panel, arena, ArenaType.seekersTokenWin, "seekersTokenWin", "1 %Ntoken", seekersTokenWin_UP, seekersTokenWin, seekersTokenWin_DOWN, 34, 43, 52);
            updownButton(panel, arena, ArenaType.killTokens, "killTokens", "1 %Ntoken", killTokens_UP, killTokens, killTokens_DOWN, 35, 44, 53);

            booleanButton(panel, arena, ArenaType.hidersPVP, "hidersPvp", 38);
            booleanButton(panel, arena, ArenaType.seekerAttackSeeker, "seekerAttackSeeker", 39);

            player.openInventory(panel);
        } else {
            MessageManager.sendFMessage(player, ConfigC.error_noArena, "name-" + arenaname);
        }
    }


    public static void updownButton(Inventory panel, Arena arena, ArenaType at, String option, String addremove, ItemStack UP, ItemStack BUTTON, ItemStack DOWN, int up, int button, int down) {
        ItemMeta UP_IM = UP.getItemMeta();
        UP_IM.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.messages.get(ConfigC.button_add), "1-" + addremove, "2-" + option));
        UP.setItemMeta(UP_IM);

        int setting = 0;
        switch (at) {
            case maxPlayers:
                setting = arena.maxPlayers;
                break;
            case minPlayers:
                setting = arena.minPlayers;
                break;
            case amountSeekersOnStart:
                setting = arena.amountSeekersOnStart;
                break;
            case timeInLobbyUntilStart:
                setting = arena.timeInLobbyUntilStart;
                break;
            case waitingTimeSeeker:
                setting = arena.waitingTimeSeeker;
                break;
            case gameTime:
                setting = arena.gameTime;
                break;
            case timeUntilHidersSword:
                setting = arena.timeUntilHidersSword;
                break;
            case blockAnnouncerTime:
                setting = arena.blockAnnouncerTime;
                break;
            case hidersTokenWin:
                setting = arena.hidersTokenWin;
                break;
            case seekersTokenWin:
                setting = arena.seekersTokenWin;
                break;
            case killTokens:
                setting = arena.killTokens;
                break;
        }

        ItemMeta BUTTON_IM = BUTTON.getItemMeta();
        BUTTON_IM.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.messages.get(ConfigC.button_setting), "1-" + option, "2-" + setting));
        BUTTON.setItemMeta(BUTTON_IM);

        ItemMeta DOWN_IM = DOWN.getItemMeta();
        DOWN_IM.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.messages.get(ConfigC.button_remove), "1-" + addremove, "2-" + option));
        DOWN.setItemMeta(DOWN_IM);

        panel.setItem(up, UP);
        panel.setItem(button, BUTTON);
        panel.setItem(down, DOWN);
    }

    public static void booleanButton(Inventory panel, Arena arena, ArenaType arenaType, String option, int slot) {
        boolean states = false;

        switch (arenaType) {
            case hidersPVP:
                states = arena.hidersCanHurtSeekers;
                break;
            case seekerAttackSeeker:
                states = arena.seekersCanHurtSeekers;
                break;
        }

        ItemStack button = new ItemStack(states ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Toggle " + ChatColor.GOLD + option + ChatColor.BLUE + " status");
        button.setItemMeta(meta);
        panel.setItem(slot, button);
    }

    public static void openDisguiseBlocks(Arena arena, Player player) {
        String arenaname = arena.arenaName;
        Inventory panel = Bukkit.createInventory(null, 36, MessageManager.replaceAll("%N&lDisguiseBlocks"));

        ItemStack arenaNameNote = new ItemStack(Material.PAPER, 1);
        ItemMeta arenaNameNote_IM = arenaNameNote.getItemMeta();
        arenaNameNote_IM.setDisplayName(MessageManager.replaceAll("%NDisguiseBlocks of arena: %A" + arenaname));
        ArrayList<String> lores = new ArrayList<>();
        lores.add(MessageManager.replaceAll("%NPlace the DisguiseBlocks inside this inventory."));
        arenaNameNote_IM.setLore(lores);
        arenaNameNote.setItemMeta(arenaNameNote_IM);
        panel.setItem(0, arenaNameNote);
        if (arena.disguiseBlocks != null) {
            for (int i = arena.disguiseBlocks.size(); i > 0; i = i - 1) {
                panel.setItem(i, arena.disguiseBlocks.get(i - 1));
            }
        }
        player.openInventory(panel);
    }

    public static void openShop(Player player) {
        Inventory shop = Bukkit.createInventory(null, 9, MessageManager.replaceAll("\u00A7r" + MemoryStorage.config.get(ConfigC.shop_title)));
        if (MemoryStorage.shop.getFile().get(player.getName() + ".tokens") == null) {
            MemoryStorage.shop.getFile().set(player.getName() + ".tokens", 0);
            MemoryStorage.shop.save();
        }
        int playerTokens = MemoryStorage.shop.getFile().getInt(player.getName() + ".tokens");
        List<String> lores;
        List<String> lores2;

        ItemStack shopTokens = new ItemStack(Material.EMERALD, 1);
        ItemMeta shopTokens_IM = shopTokens.getItemMeta();
        shopTokens_IM.setDisplayName(MessageManager.replaceAll("%N&lTokens: %A" + playerTokens));
        shopTokens.setItemMeta(shopTokens_IM);

        ItemStack shopBlockChooser = new ItemStack(Material.getMaterial((String) MemoryStorage.config.get(ConfigC.shop_blockChooserv1IDname)), 1);
        ItemMeta shopBlockChooser_IM = shopBlockChooser.getItemMeta();
        shopBlockChooser_IM.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Name)));
        lores = MemoryStorage.config.getFile().getStringList(ConfigC.shop_blockChooserv1Description.location);
        lores2 = new ArrayList<>();
        for (String lore : lores) {
            lores2.add(MessageManager.replaceAll(lore));
        }

        lores2.add(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_price), "amount-" + MemoryStorage.config.get(ConfigC.shop_blockChooserv1Price)));

        shopBlockChooser_IM.setLore(lores2);
        shopBlockChooser.setItemMeta(shopBlockChooser_IM);

        ItemStack shopBlockHuntPass = new ItemStack(Material.getMaterial((String) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2IDName)), 1);
        ItemMeta shopBlockHuntPass_IM = shopBlockHuntPass.getItemMeta();
        shopBlockHuntPass_IM.setDisplayName(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Name)));
        lores = MemoryStorage.config.getFile().getStringList(ConfigC.shop_BlockHuntPassv2Description.location);
        lores2 = new ArrayList<>();
        for (String lore : lores) {
            lores2.add(MessageManager.replaceAll(lore));
        }

        lores2.add(MessageManager.replaceAll((String) MemoryStorage.config.get(ConfigC.shop_price), "amount-" + MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Price)));

        shopBlockHuntPass_IM.setLore(lores2);
        shopBlockHuntPass.setItemMeta(shopBlockHuntPass_IM);

        shop.setItem(0, shopTokens);
        if ((Boolean) MemoryStorage.config.get(ConfigC.shop_blockChooserv1Enabled)
                && (MemoryStorage.shop.getFile().get(player.getName() + ".blockchooser") == null && !PermissionsManager.hasPerm(player, PermissionsC.Permissions.shopblockchooser, false))) {
            shop.setItem(1, shopBlockChooser);
        }
        if ((Boolean) MemoryStorage.config.get(ConfigC.shop_BlockHuntPassv2Enabled)) {
            shop.setItem(2, shopBlockHuntPass);
        }
        player.openInventory(shop);
    }
}
