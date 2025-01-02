package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MobStatsMenuListener implements Listener {
    private final XMobGeneration plugin;

    public MobStatsMenuListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        String title = event.getView().getTitle();
        if (!title.startsWith("§8Mob Stats - ")) return;

        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        
        if (clicked == null) return;

        String areaName = title.substring("§8Mob Stats - ".length());
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) return;

        switch (event.getSlot()) {
            case 10: // Name Toggle
                area.getMobStats().setShowName(!area.getMobStats().isShowName());
                break;
                
            case 12: // Health
                if (event.isLeftClick()) {
                    area.getMobStats().setHealth(area.getMobStats().getHealth() + 5);
                } else if (event.isRightClick()) {
                    area.getMobStats().setHealth(Math.max(1, area.getMobStats().getHealth() - 5));
                }
                break;
                
            case 14: // Damage
                if (event.isLeftClick()) {
                    area.getMobStats().setDamage(area.getMobStats().getDamage() + 1);
                } else if (event.isRightClick()) {
                    area.getMobStats().setDamage(Math.max(0, area.getMobStats().getDamage() - 1));
                }
                break;
                
            case 16: // Level
                if (event.isLeftClick()) {
                    area.getMobStats().setLevel(area.getMobStats().getLevel() + 1);
                } else if (event.isRightClick()) {
                    area.getMobStats().setLevel(Math.max(1, area.getMobStats().getLevel() - 1));
                }
                break;
        }

        // Save changes and refresh menu
        plugin.getAreaManager().saveAreas();
        plugin.getGUIManager().openMobStatsMenu(player, area);
        
        // Restart the area to apply changes to existing mobs
        plugin.getSpawnManager().restartArea(area);
    }
}