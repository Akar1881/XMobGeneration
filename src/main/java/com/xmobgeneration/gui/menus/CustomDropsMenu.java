package com.xmobgeneration.gui.menus;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomDropsMenu {
    private final XMobGeneration plugin;
    private static final int DROPS_INVENTORY_SIZE = 54;
    private static final int TOGGLE_BUTTON_SLOT = 49;
    private static final int SAVE_BUTTON_SLOT = 53;

    public CustomDropsMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, DROPS_INVENTORY_SIZE, 
            "§8Custom Drops - " + area.getName());

        // Add existing custom drops
        int slot = 0;
        List<ItemStack> items = area.getCustomDrops().getItems();
        List<Double> chances = area.getCustomDrops().getChances();
        
        for (int i = 0; i < items.size(); i++) {
            if (slot < 45) {
                ItemStack item = items.get(i).clone();
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                    
                    // Remove any existing chance lore
                    lore.removeIf(line -> line.contains("Drop Chance:") || line.contains("Edit chance"));
                    
                    // Add current chance lore
                    lore.add("§7Drop Chance: §e" + chances.get(i) + "%");
                    lore.add("§7Edit chance in areas.yml");
                    
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                gui.setItem(slot++, item);
            }
        }

        // Add toggle button
        boolean enabled = area.getCustomDrops().isEnabled();
        gui.setItem(TOGGLE_BUTTON_SLOT, createToggleButton(enabled));

        // Add save button
        gui.setItem(SAVE_BUTTON_SLOT, createSaveButton());

        player.openInventory(gui);
    }

    private ItemStack createToggleButton(boolean enabled) {
        ItemStack button = new ItemStack(enabled ? Material.LIME_DYE : Material.GRAY_DYE);
        ItemMeta meta = button.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(enabled ? "§aCustom Drops Enabled" : "§cCustom Drops Disabled");
            meta.setLore(Arrays.asList("§7Click to toggle custom drops"));
            button.setItemMeta(meta);
        }
        return button;
    }

    private ItemStack createSaveButton() {
        ItemStack button = new ItemStack(Material.DIAMOND);
        ItemMeta meta = button.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aSave Changes");
            meta.setLore(Arrays.asList(
                "§7Click to save custom drops",
                "§7All new items will have 100% drop chance",
                "§7Edit chances in areas.yml"
            ));
            button.setItemMeta(meta);
        }
        return button;
    }

    public void saveCustomDrops(Player player, SpawnArea area) {
        Inventory inv = player.getOpenInventory().getTopInventory();
        List<ItemStack> items = new ArrayList<>();
        List<Double> chances = new ArrayList<>();

        for (int i = 0; i < 45; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                ItemStack cleanItem = item.clone();
                ItemMeta meta = cleanItem.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                    
                    // Remove any chance-related lore
                    lore.removeIf(line -> line.contains("Drop Chance:") || line.contains("Edit chance"));
                    
                    // Set cleaned lore or null if empty
                    meta.setLore(lore.isEmpty() ? null : lore);
                    cleanItem.setItemMeta(meta);
                }
                items.add(cleanItem);
                chances.add(100.0); // Set default 100% chance for all items
            }
        }

        area.getCustomDrops().setItems(items, chances);
        plugin.getAreaManager().saveAreas();
    }
}