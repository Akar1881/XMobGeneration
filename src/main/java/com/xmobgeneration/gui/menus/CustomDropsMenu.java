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
import java.util.HashMap;
import java.util.Map;

public class CustomDropsMenu {
    private final XMobGeneration plugin;
    private static final int DROPS_INVENTORY_SIZE = 54;
    private static final int TOGGLE_BUTTON_SLOT = 49;
    private static final int SAVE_BUTTON_SLOT = 53;
    private static final Map<String, Double> pendingChances = new HashMap<>();

    public CustomDropsMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, DROPS_INVENTORY_SIZE, 
            "§8Custom Drops - " + area.getName());

        // Add existing custom drops with their chances
        int slot = 0;
        List<ItemStack> items = area.getCustomDrops().getItems();
        List<Double> chances = area.getCustomDrops().getChances();
        
        for (int i = 0; i < items.size(); i++) {
            if (slot < 45) {
                ItemStack item = items.get(i).clone();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add("§7Drop Chance: §e" + chances.get(i) + "%");
                lore.add("§7SHIFT + RIGHT CLICK to set chance");
                meta.setLore(lore);
                item.setItemMeta(meta);
                gui.setItem(slot++, item);
            }
        }

        // Add toggle button
        boolean enabled = area.getCustomDrops().isEnabled();
        ItemStack toggleButton = new ItemStack(enabled ? Material.LIME_DYE : Material.GRAY_DYE);
        ItemMeta toggleMeta = toggleButton.getItemMeta();
        toggleMeta.setDisplayName(enabled ? "§aCustom Drops Enabled" : "§cCustom Drops Disabled");
        toggleMeta.setLore(Arrays.asList("§7Click to toggle custom drops"));
        toggleButton.setItemMeta(toggleMeta);
        gui.setItem(TOGGLE_BUTTON_SLOT, toggleButton);

        // Add save button
        ItemStack saveButton = new ItemStack(Material.DIAMOND);
        ItemMeta saveMeta = saveButton.getItemMeta();
        saveMeta.setDisplayName("§aSave Changes");
        saveMeta.setLore(Arrays.asList(
            "§7Click to save custom drops",
            "§7and refresh the menu"
        ));
        saveButton.setItemMeta(saveMeta);
        gui.setItem(SAVE_BUTTON_SLOT, saveButton);

        player.openInventory(gui);
    }

    public void handleChanceInput(Player player, String input, SpawnArea area, ItemStack item) {
        try {
            double chance = Double.parseDouble(input);
            if (chance < 0 || chance > 100) {
                player.sendMessage("§cChance must be between 0 and 100!");
                return;
            }

            pendingChances.put(player.getName() + "_" + item.toString(), chance);
            player.sendMessage("§aChance set to " + chance + "%");
            
            // Save and refresh GUI
            saveCustomDrops(player, area);
            openMenu(player, area);

        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid number format! Please enter a number between 0 and 100.");
        }
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
                List<String> lore = meta.getLore();
                if (lore != null) {
                    // Remove the chance lore
                    lore.removeIf(line -> line.contains("Drop Chance:") || line.contains("SHIFT + RIGHT"));
                    meta.setLore(lore.isEmpty() ? null : lore);
                }
                cleanItem.setItemMeta(meta);
                items.add(cleanItem);

                // Get chance from pending or existing
                String key = player.getName() + "_" + item.toString();
                double chance = pendingChances.getOrDefault(key, 
                    area.getCustomDrops().getChance(items.size() - 1));
                chances.add(chance);
            }
        }

        area.getCustomDrops().setItems(items, chances);
        plugin.getAreaManager().saveAreas();
        pendingChances.clear();
    }
}