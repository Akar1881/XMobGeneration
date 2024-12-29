package com.xmobgeneration.gui.menus;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MobStatsMenu {
    private final XMobGeneration plugin;

    public MobStatsMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, SpawnArea area) {
        Inventory gui = Bukkit.createInventory(null, 27, "§8Mob Stats - " + area.getName());

        // Toggle Name Display
        ItemStack nameToggle = GuiUtils.createGuiItem(
            Material.NAME_TAG,
            area.getMobStats().isShowName() ? "§aMob Names Enabled" : "§cMob Names Disabled",
            Arrays.asList(
                "§7Current Name: §f" + area.getMobStats().getMobName(),
                "§7Click to toggle name display"
            )
        );
        gui.setItem(10, nameToggle);

        // Health Control
        ItemStack health = GuiUtils.createGuiItem(
            Material.RED_DYE,
            "§cMob Health",
            Arrays.asList(
                "§7Current: §c" + area.getMobStats().getHealth() + "❤",
                "§7Left-Click: §a+5",
                "§7Right-Click: §c-5"
            )
        );
        gui.setItem(12, health);

        // Damage Control
        ItemStack damage = GuiUtils.createGuiItem(
            Material.IRON_SWORD,
            "§eMob Damage",
            Arrays.asList(
                "§7Current: §e" + area.getMobStats().getDamage(),
                "§7Left-Click: §a+1",
                "§7Right-Click: §c-1"
            )
        );
        gui.setItem(14, damage);

        // Level Control
        ItemStack level = GuiUtils.createGuiItem(
            Material.EXPERIENCE_BOTTLE,
            "§bMob Level",
            Arrays.asList(
                "§7Current: §b" + area.getMobStats().getLevel(),
                "§7Left-Click: §a+1",
                "§7Right-Click: §c-1"
            )
        );
        gui.setItem(16, level);

        player.openInventory(gui);
    }
}