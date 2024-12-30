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

public class CustomMobEquipmentMenu {
    private final XMobGeneration plugin;

    public CustomMobEquipmentMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, 9, "§8Mob Equipment - " + area.getName());

        // Helmet slot
        ItemStack helmetSlot = area.getMobEquipment().getHelmet() != null ? 
            area.getMobEquipment().getHelmet() : 
            createEquipmentSlot(Material.ARMOR_STAND, "§eHelmet", "Click any helmet to equip");
        gui.setItem(0, helmetSlot);

        // Chestplate slot
        ItemStack chestplateSlot = area.getMobEquipment().getChestplate() != null ? 
            area.getMobEquipment().getChestplate() : 
            createEquipmentSlot(Material.ARMOR_STAND, "§eChestplate", "Click any chestplate to equip");
        gui.setItem(1, chestplateSlot);

        // Leggings slot
        ItemStack leggingsSlot = area.getMobEquipment().getLeggings() != null ? 
            area.getMobEquipment().getLeggings() : 
            createEquipmentSlot(Material.ARMOR_STAND, "§eLeggings", "Click any leggings to equip");
        gui.setItem(2, leggingsSlot);

        // Boots slot
        ItemStack bootsSlot = area.getMobEquipment().getBoots() != null ? 
            area.getMobEquipment().getBoots() : 
            createEquipmentSlot(Material.ARMOR_STAND, "§eBoots", "Click any boots to equip");
        gui.setItem(3, bootsSlot);

        // Off-hand slot
        ItemStack offHandSlot = area.getMobEquipment().getOffHand() != null ? 
            area.getMobEquipment().getOffHand() : 
            createEquipmentSlot(Material.ARMOR_STAND, "§eOff-Hand Item", "Click any item to equip in off-hand");
        gui.setItem(4, offHandSlot);

        player.openInventory(gui);
    }

    private ItemStack createEquipmentSlot(Material material, String name, String lore) {
        return GuiUtils.createGuiItem(material, name, Arrays.asList("§7" + lore));
    }
}