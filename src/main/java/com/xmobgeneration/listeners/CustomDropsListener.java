package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CustomDropsListener implements Listener {
    private final XMobGeneration plugin;

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
                // Clear default drops and add custom drops
                event.getDrops().clear();
                for (ItemStack item : area.getCustomDrops().getItems()) {
                    event.getDrops().add(item.clone());
                }
            }
        }
    }
}