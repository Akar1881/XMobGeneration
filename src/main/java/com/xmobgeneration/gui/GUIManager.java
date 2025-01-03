package com.xmobgeneration.gui;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.gui.menus.CustomDropsMenu;
import com.xmobgeneration.gui.menus.CustomMobEquipmentMenu;
import com.xmobgeneration.gui.menus.MobStatsMenu;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    private static final int GUI_SIZE = 54;
    private final CustomDropsMenu customDropsMenu;

    public GUIManager(XMobGeneration plugin) {
        this.plugin = plugin;
        this.customDropsMenu = new CustomDropsMenu(plugin);
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

        // Add XP Bottle item to show/edit XP amount
    ItemStack xpBottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemMeta xpMeta = xpBottle.getItemMeta();
    xpMeta.setDisplayName("§eXP Amount");
    List<String> xpLore = new ArrayList<>();
    xpLore.add("§7Current XP: §b" + area.getXpAmount());
    xpLore.add("");
    xpLore.add("§7Left-Click: §a+10 XP");
    xpLore.add("§7Right-Click: §c-10 XP");
    xpLore.add("§7Use /xmg xp [area] [amount] to set");
    xpMeta.setLore(xpLore);
    xpBottle.setItemMeta(xpMeta);
    gui.setItem(29, xpBottle);

        // Custom Drops Button
        ItemStack customDrops = new ItemStack(Material.CHEST);
        ItemMeta customDropsMeta = customDrops.getItemMeta();
        customDropsMeta.setDisplayName("§eCustom Drops");
        List<String> customDropsLore = new ArrayList<>();
        customDropsLore.add("§7Click to configure custom drops");
        customDropsLore.add("");
        customDropsLore.add(area.getCustomDrops().isEnabled() ? "§aEnabled" : "§cDisabled");
        customDropsMeta.setLore(customDropsLore);
        customDrops.setItemMeta(customDropsMeta);
        gui.setItem(31, customDrops);

        // Mob Stats Button
        ItemStack mobStats = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta mobStatsMeta = mobStats.getItemMeta();
        mobStatsMeta.setDisplayName("§eMob Stats");
        List<String> mobStatsLore = new ArrayList<>();
        mobStatsLore.add("§7Click to configure mob stats");
        mobStatsLore.add("");
        mobStatsLore.add("§7Health: §c" + area.getMobStats().getHealth() + "❤");
        mobStatsLore.add("§7Damage: §e" + area.getMobStats().getDamage());
        mobStatsLore.add("§7Level: §b" + area.getMobStats().getLevel());
        mobStatsMeta.setLore(mobStatsLore);
        mobStats.setItemMeta(mobStatsMeta);
        gui.setItem(33, mobStats);

        // Add Equipment Button
        ItemStack equipmentButton = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta equipmentMeta = equipmentButton.getItemMeta();
        equipmentMeta.setDisplayName("§eMob Equipment");
        List<String> equipmentLore = new ArrayList<>();
        equipmentLore.add("§7Click to configure mob equipment");
        equipmentMeta.setLore(equipmentLore);
        equipmentButton.setItemMeta(equipmentMeta);
        gui.setItem(35, equipmentButton);

        // Toggle Spawning
        ItemStack toggleButton = new ItemStack(area.isEnabled() ? Material.LIME_DYE : Material.GRAY_DYE);
        ItemMeta toggleMeta = toggleButton.getItemMeta();
        toggleMeta.setDisplayName(area.isEnabled() ? "§aEnabled" : "§cDisabled");
        toggleMeta.setLore(Arrays.asList("§7Click to toggle spawning"));
        toggleButton.setItemMeta(toggleMeta);
        gui.setItem(49, toggleButton);

        player.openInventory(gui);
    }

    public void openCustomMobEquipmentMenu(Player player, SpawnArea area) {
        new CustomMobEquipmentMenu(plugin).openMenu(player, area);
    }

    public void openCustomDropsMenu(Player player, SpawnArea area) {
        customDropsMenu.openMenu(player, area);
    }

    public CustomDropsMenu getCustomDropsMenu() {
        return customDropsMenu;
    }

    public void openMobStatsMenu(Player player, SpawnArea area) {
        new MobStatsMenu(plugin).openMenu(player, area);
    }

    public void openAreaListGUI(Player player, String action) {
        Map<String, SpawnArea> areas = plugin.getAreaManager().getAllAreas();
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, "§8Areas - " + action);

        int slot = 0;
        for (SpawnArea area : areas.values()) {
            if (slot >= 45) break;
            
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

        player.openInventory(gui);
    }
}