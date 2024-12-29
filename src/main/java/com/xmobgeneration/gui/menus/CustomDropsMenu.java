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

import java.util.Arrays;
import java.util.List;

public class CustomDropsMenu {
    private final XMobGeneration plugin;
    private static final int DROPS_INVENTORY_SIZE = 54;
    private static final int TOGGLE_BUTTON_SLOT = 49;

    public CustomDropsMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, DROPS_INVENTORY_SIZE, 
            "§8Custom Drops - " + area.getName());

        // Add existing custom drops
        int slot = 0;
        for (ItemStack item : area.getCustomDrops().getItems()) {
            if (slot < 45) { // Reserve bottom row for controls
                gui.setItem(slot++, item.clone());
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
        ItemStack saveButton = new ItemStack(Material.EMERALD);
        ItemMeta saveMeta = saveButton.getItemMeta();
        saveMeta.setDisplayName("§aSave Changes");
        saveMeta.setLore(Arrays.asList("§7Click to save custom drops"));
        saveButton.setItemMeta(saveMeta);
        gui.setItem(53, saveButton);

        player.openInventory(gui);
    }
}