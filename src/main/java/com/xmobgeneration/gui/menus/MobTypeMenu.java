package com.xmobgeneration.gui.menus;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class MobTypeMenu {
    private final XMobGeneration plugin;
    private static final List<EntityType> ALLOWED_MOBS = Arrays.asList(
        EntityType.ZOMBIE,
        EntityType.SKELETON,
        EntityType.SPIDER,
        EntityType.CREEPER,
        EntityType.ENDERMAN,
        EntityType.WITCH,
        EntityType.BLAZE,
        EntityType.SLIME
    );

    public MobTypeMenu(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§8Select Mob Type");

        int slot = 0;
        for (EntityType type : ALLOWED_MOBS) {
            ItemStack item = new ItemStack(getMobTypeItem(type));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e" + type.name());
            item.setItemMeta(meta);
            gui.setItem(slot++, item);
        }

        player.openInventory(gui);
    }

    private Material getMobTypeItem(EntityType type) {
        switch (type) {
            case ZOMBIE: return Material.ZOMBIE_SPAWN_EGG;
            case SKELETON: return Material.SKELETON_SPAWN_EGG;
            case SPIDER: return Material.SPIDER_SPAWN_EGG;
            case CREEPER: return Material.CREEPER_SPAWN_EGG;
            case ENDERMAN: return Material.ENDERMAN_SPAWN_EGG;
            case WITCH: return Material.WITCH_SPAWN_EGG;
            case BLAZE: return Material.BLAZE_SPAWN_EGG;
            case SLIME: return Material.SLIME_SPAWN_EGG;
            default: return Material.BARRIER;
        }
    }
}