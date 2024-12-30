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
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CustomDropsMenu {
    private final XMobGeneration plugin;
    private static final int DROPS_INVENTORY_SIZE = 54;
    private static final int TOGGLE_BUTTON_SLOT = 49;
    private static final int SAVE_BUTTON_SLOT = 53;
    
    private final Map<String, Map<Integer, Double>> areaItemChances = new HashMap<>();

    public CustomDropsMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, DROPS_INVENTORY_SIZE, 
            "§8Custom Drops - " + area.getName());

        Map<Integer, Double> itemChances = areaItemChances.computeIfAbsent(
            area.getName(), 
            k -> new HashMap<>()
        );

        populateInventory(gui, area, itemChances);
        player.openInventory(gui);
    }

    private void populateInventory(Inventory gui, SpawnArea area, Map<Integer, Double> itemChances) {
        int slot = 0;
        List<ItemStack> items = area.getCustomDrops().getItems();
        List<Double> chances = area.getCustomDrops().getChances();
        
        for (int i = 0; i < items.size(); i++) {
            if (slot < 45) {
                ItemStack item = items.get(i).clone();
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                    lore.removeIf(line -> line.contains("Drop Chance:") || line.contains("Edit chance"));
                    
                    double chance = chances.get(i);
                    itemChances.put(slot, chance);
                    
                    lore.add("§7Drop Chance: §e" + chance + "%");
                    lore.add("§7Edit chance in areas.yml");
                    
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                gui.setItem(slot++, item);
            }
        }

        gui.setItem(TOGGLE_BUTTON_SLOT, createToggleButton(area.getCustomDrops().isEnabled()));
        gui.setItem(SAVE_BUTTON_SLOT, createSaveButton());
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
                "§7Preserves existing drop chances",
                "§7New items will have 100% drop chance"
            ));
            button.setItemMeta(meta);
        }
        return button;
    }

    public void handleInventoryClose(InventoryCloseEvent event, SpawnArea area) {
        List<ItemStack> items = new ArrayList<>();
        List<Double> chances = new ArrayList<>();
        Map<Integer, Double> itemChances = areaItemChances.getOrDefault(area.getName(), new HashMap<>());
        
        for (int i = 0; i < 45; i++) {
            ItemStack item = event.getInventory().getItem(i);
            if (item != null) {
                items.add(cleanItem(item.clone()));
                chances.add(itemChances.getOrDefault(i, 100.0));
            }
        }
        
        area.getCustomDrops().setItems(items, chances);
        plugin.getAreaManager().saveAreas();
        areaItemChances.remove(area.getName());
    }

    private ItemStack cleanItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            lore.removeIf(line -> line.contains("Drop Chance:") || line.contains("Edit chance"));
            meta.setLore(lore.isEmpty() ? null : lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public void saveCustomDrops(Player player, SpawnArea area) {
        Inventory inv = player.getOpenInventory().getTopInventory();
        List<ItemStack> items = new ArrayList<>();
        List<Double> chances = new ArrayList<>();
        Map<Integer, Double> itemChances = areaItemChances.getOrDefault(area.getName(), new HashMap<>());

        for (int i = 0; i < 45; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                items.add(cleanItem(item.clone()));
                chances.add(itemChances.getOrDefault(i, 100.0));
            }
        }

        area.getCustomDrops().setItems(items, chances);
        plugin.getAreaManager().saveAreas();
        areaItemChances.remove(area.getName());
    }
}