package com.xmobgeneration.gui;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GUIManager {
    private final XMobGeneration plugin;
    private static final int GUI_SIZE = 54; // Updated to support 45 areas (6 rows x 9 columns)

    public GUIManager(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void openMainGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§8XMobGeneration");

        // Create Area button
        ItemStack createButton = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta createMeta = createButton.getItemMeta();
        createMeta.setDisplayName("§aCreate New Area");
        List<String> createLore = new ArrayList<>();
        createLore.add("§7Click to create a new spawn area");
        createLore.add("§7Areas: §f" + plugin.getAreaManager().getAllAreas().size() + "/" + plugin.getAreaManager().getMaxAreas());
        createMeta.setLore(createLore);
        createButton.setItemMeta(createMeta);
        gui.setItem(11, createButton);

        // Edit Area button
        ItemStack editButton = new ItemStack(Material.ANVIL);
        ItemMeta editMeta = editButton.getItemMeta();
        editMeta.setDisplayName("§6Edit Area");
        editMeta.setLore(Arrays.asList("§7Click to edit an existing area"));
        editButton.setItemMeta(editMeta);
        gui.setItem(13, editButton);

        // Delete Area button
        ItemStack deleteButton = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta deleteMeta = deleteButton.getItemMeta();
        deleteMeta.setDisplayName("§cDelete Area");
        deleteMeta.setLore(Arrays.asList("§7Click to delete an area"));
        deleteButton.setItemMeta(deleteMeta);
        gui.setItem(15, deleteButton);

        player.openInventory(gui);
    }

    public void openAreaListGUI(Player player, String action) {
        Map<String, SpawnArea> areas = plugin.getAreaManager().getAllAreas();
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, "§8Areas - " + action);

        int slot = 0;
        for (SpawnArea area : areas.values()) {
            if (slot >= 45) break; // Ensure we don't exceed 45 slots
            
            ItemStack item;
            if (action.equals("list")) {
                item = new ItemStack(area.isEnabled() ? Material.LIME_DYE : Material.GRAY_DYE);
            } else {
                item = new ItemStack(Material.PAPER);
            }
            
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e" + area.getName());
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Mob Type: §f" + area.getMobType());
            lore.add("§7Spawn Count: §f" + area.getSpawnCount());
            lore.add("§7Respawn Delay: §f" + area.getRespawnDelay() + "s");
            lore.add("");
            lore.add(area.isEnabled() ? "§aEnabled" : "§cDisabled");
            if (action.equals("list")) {
                lore.add("§7Click to toggle spawning");
            }
            
            meta.setLore(lore);
            item.setItemMeta(meta);
            gui.setItem(slot++, item);
        }

        // Add navigation buttons in the bottom row if needed
        // Slots 45-53 can be used for navigation or additional controls

        player.openInventory(gui);
    }

    public void openAreaEditGUI(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, "§8Edit Area - " + area.getName());

        // Mob Type
        ItemStack mobType = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
        ItemMeta mobTypeMeta = mobType.getItemMeta();
        mobTypeMeta.setDisplayName("§eMob Type: §f" + area.getMobType());
        mobType.setItemMeta(mobTypeMeta);
        gui.setItem(11, mobType);

        // Spawn Count
        ItemStack spawnCount = new ItemStack(Material.REPEATER);
        ItemMeta spawnCountMeta = spawnCount.getItemMeta();
        spawnCountMeta.setDisplayName("§eSpawn Count: §f" + area.getSpawnCount());
        spawnCount.setItemMeta(spawnCountMeta);
        gui.setItem(13, spawnCount);

        // Respawn Delay
        ItemStack respawnDelay = new ItemStack(Material.CLOCK);
        ItemMeta respawnDelayMeta = respawnDelay.getItemMeta();
        respawnDelayMeta.setDisplayName("§eRespawn Delay: §f" + area.getRespawnDelay() + "s");
        respawnDelay.setItemMeta(respawnDelayMeta);
        gui.setItem(15, respawnDelay);

        // Toggle Spawning
        ItemStack toggleButton = new ItemStack(area.isEnabled() ? Material.LIME_DYE : Material.GRAY_DYE);
        ItemMeta toggleMeta = toggleButton.getItemMeta();
        toggleMeta.setDisplayName(area.isEnabled() ? "§aEnabled" : "§cDisabled");
        toggleMeta.setLore(Arrays.asList("§7Click to toggle spawning"));
        toggleButton.setItemMeta(toggleMeta);
        gui.setItem(49, toggleButton); // Centered in the bottom row

        player.openInventory(gui);
    }
}