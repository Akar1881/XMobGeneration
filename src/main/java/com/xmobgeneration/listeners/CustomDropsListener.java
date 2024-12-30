package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class CustomDropsListener implements Listener {
    private final XMobGeneration plugin;
    private final Random random = new Random();

    public CustomDropsListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        SpawnedMob spawnedMob = plugin.getSpawnManager().getMobTracker().getMob(entity.getUniqueId());
        
        if (spawnedMob != null) {
            SpawnArea area = plugin.getAreaManager().getArea(spawnedMob.getAreaName());
            if (area != null && area.getCustomDrops().isEnabled()) {
                // Clear default drops
                event.getDrops().clear();
                
                // Get items and their chances
                List<ItemStack> items = area.getCustomDrops().getItems();
                List<Double> chances = area.getCustomDrops().getChances();
                
                // Process each item with its chance
                for (int i = 0; i < items.size(); i++) {
                    double chance = chances.get(i);
                    if (random.nextDouble() * 100 <= chance) {
                        event.getDrops().add(items.get(i).clone());
                    }
                }
            }
        }
    }
}