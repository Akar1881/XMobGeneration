package com.xmobgeneration.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {
    public static List<ItemStack> getInventoryItems(Inventory inventory, int startSlot, int endSlot) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                items.add(item.clone());
            }
        }
        return items;
    }

    public static List<Double> generateDefaultChances(int count) {
        List<Double> chances = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            chances.add(100.0);
        }
        return chances;
    }
}