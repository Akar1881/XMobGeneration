package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.managers.spawn.LocationFinder;
import com.xmobgeneration.managers.spawn.MobTracker;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.MobStats;
import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SpawnManager {
    private final XMobGeneration plugin;
    private final Map<String, BukkitRunnable> spawnTasks = new HashMap<>();
    private final MobTracker mobTracker;
    private final LocationFinder locationFinder;

    public SpawnManager(XMobGeneration plugin) {
        this.plugin = plugin;
        this.mobTracker = new MobTracker();
        this.locationFinder = new LocationFinder();
    }

    public void startSpawning(SpawnArea area) {
        stopSpawning(area.getName());
        
        if (!area.isEnabled()) {
            return;
        }

        plugin.getLogger().info("Starting initial spawn for area: " + area.getName());
        performInitialSpawn(area);
    }

    private void performInitialSpawn(SpawnArea area) {
        int attempts = 0;
        int maxAttempts = area.getSpawnCount() * 3;
        int spawned = 0;

        while (spawned < area.getSpawnCount() && attempts < maxAttempts) {
            attempts++;
            Location spawnLoc = locationFinder.findSafeSpawnLocation(area);
            
            if (spawnLoc != null) {
                Entity entity = spawnEntity(spawnLoc, area);
                mobTracker.trackMob(entity, area.getName(), spawnLoc);
                spawned++;
            }
        }

        plugin.getLogger().info(String.format("Initial spawn complete - Spawned %d/%d mobs in area %s", 
            spawned, area.getSpawnCount(), area.getName()));
    }

    private Entity spawnEntity(Location location, SpawnArea area) {
        Entity entity = location.getWorld().spawnEntity(location, area.getMobType());
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            MobStats stats = area.getMobStats();
            
            // Set custom name
            if (stats.isShowName()) {
                livingEntity.setCustomName(stats.getDisplayName());
                livingEntity.setCustomNameVisible(true);
            }
            
            // Set health
            livingEntity.setMaxHealth(stats.getHealth());
            livingEntity.setHealth(stats.getHealth());
            
            // Store damage in metadata for the damage listener
            livingEntity.setMetadata("mobDamage", new FixedMetadataValue(plugin, stats.getDamage()));
        }
        return entity;
    }

    public void handleMobDeath(Entity entity) {
        mobTracker.handleMobDeath(entity);
    }

    public void stopSpawning(String areaName) {
        BukkitRunnable task = spawnTasks.remove(areaName);
        if (task != null) {
            task.cancel();
        }
        mobTracker.clearAreaMobs(areaName);
    }

    public boolean toggleSpawning(SpawnArea area) {
        area.setEnabled(!area.isEnabled());
        
        if (area.isEnabled()) {
            startSpawning(area);
        } else {
            stopSpawning(area.getName());
        }
        
        return area.isEnabled();
    }

    public void restartArea(SpawnArea area) {
        mobTracker.despawnAreaMobs(area.getName());
        startSpawning(area);
    }

    public MobTracker getMobTracker() {
        return mobTracker;
    }
}