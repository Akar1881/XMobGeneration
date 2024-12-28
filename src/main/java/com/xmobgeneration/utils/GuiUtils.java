package com.xmobgeneration.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GuiUtils {
    public static ItemStack createGuiItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.colorize(name));
        if (lore != null) {
            meta.setLore(lore.stream()
                .map(MessageUtils::colorize)
                .collect(java.util.stream.Collectors.toList()));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createGuiItem(Material material, String name) {
        return createGuiItem(material, name, null);
    }

    public static boolean isGuiItem(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
    }
}