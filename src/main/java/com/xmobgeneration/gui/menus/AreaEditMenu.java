package com.xmobgeneration.gui.menus;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AreaEditMenu {
    private final XMobGeneration plugin;

    public AreaEditMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, 27, "§8Edit Area - " + area.getName());

        // Mob Type Selection
        ItemStack mobType = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
        ItemMeta mobTypeMeta = mobType.getItemMeta();
        mobTypeMeta.setDisplayName("§eChange Mob Type");
        mobTypeMeta.setLore(Arrays.asList(
            "§7Current: §f" + area.getMobType(),
            "§7Click to change"
        ));
        mobType.setItemMeta(mobTypeMeta);
        gui.setItem(11, mobType);

        // Spawn Count
        ItemStack spawnCount = new ItemStack(Material.REPEATER);
        ItemMeta spawnCountMeta = spawnCount.getItemMeta();
        spawnCountMeta.setDisplayName("§eSpawn Count");
        spawnCountMeta.setLore(Arrays.asList(
            "§7Current: §f" + area.getSpawnCount(),
            "§7Left-click to increase",
            "§7Right-click to decrease"
        ));
        spawnCount.setItemMeta(spawnCountMeta);
        gui.setItem(13, spawnCount);

        // Respawn Delay
        ItemStack respawnDelay = new ItemStack(Material.CLOCK);
        ItemMeta respawnDelayMeta = respawnDelay.getItemMeta();
        respawnDelayMeta.setDisplayName("§eRespawn Delay");
        respawnDelayMeta.setLore(Arrays.asList(
            "§7Current: §f" + area.getRespawnDelay() + "s",
            "§7Left-click to increase",
            "§7Right-click to decrease"
        ));
        respawnDelay.setItemMeta(respawnDelayMeta);
        gui.setItem(15, respawnDelay);

        // Save Button
        ItemStack saveButton = new ItemStack(Material.EMERALD);
        ItemMeta saveMeta = saveButton.getItemMeta();
        saveMeta.setDisplayName("§aSave Changes");
        saveButton.setItemMeta(saveMeta);
        gui.setItem(26, saveButton);

        player.openInventory(gui);
    }
}