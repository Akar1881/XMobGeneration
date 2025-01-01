package com.xmobgeneration.managers.drops;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DropUtil {
    public static void giveItemToPlayer(Player player, ItemStack item, boolean isBossDrop) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(item);
            String itemName = getItemName(item);
            player.sendMessage(isBossDrop ? 
                "§aYou received " + itemName + " §afrom the boss!" :
                "§aYou received " + itemName + "!");
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
            player.sendMessage("§eYour inventory was full! The item was dropped at your feet.");
        }
    }

    public static String getItemName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName() ? meta.getDisplayName() : 
            item.getType().toString().toLowerCase().replace("_", " ");
    }
}