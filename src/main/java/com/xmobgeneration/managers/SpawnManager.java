package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.managers.spawn.LocationFinder;
import com.xmobgeneration.managers.spawn.MobTracker;
import com.xmobgeneration.managers.spawn.RespawnTask;
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
    private RespawnTask respawnTask;

    public SpawnManager(XMobGeneration plugin) {
        this.plugin = plugin;
        this.mobTracker = new MobTracker();
        this.locationFinder = new LocationFinder();
        startRespawnTask();
    }

    private void startRespawnTask() {
        if (respawnTask != null) {
            respawnTask.cancel();
        }
        respawnTask = new RespawnTask(plugin, mobTracker, locationFinder);
        respawnTask.runTaskTimer(plugin, 20L, 20L);
    }

    public void startSpawning(SpawnArea area) {
        stopSpawning(area.getName());
        
        if (!area.isEnabled()) {
            return;
        }

        // Check if area already has the correct number of mobs
        int currentMobs = mobTracker.getAreaMobCount(area.getName());
        if (currentMobs >= area.getSpawnCount()) {
            plugin.getLogger().info("Area " + area.getName() + " already has " + currentMobs + " mobs");
            return;
        }

        plugin.getLogger().info("Starting initial spawn for area: " + area.getName());
        performInitialSpawn(area);
    }

    private void performInitialSpawn(SpawnArea area) {
        int currentMobs = mobTracker.getAreaMobCount(area.getName());
        int neededMobs = area.getSpawnCount() - currentMobs;
        
        if (neededMobs <= 0) {
            return;
        }

        int attempts = 0;
        int maxAttempts = neededMobs * 3;
        int spawned = 0;

        while (spawned < neededMobs && attempts < maxAttempts) {
            attempts++;
            Location spawnLoc = locationFinder.findSafeSpawnLocation(area);
            
            if (spawnLoc != null) {
                Entity entity = spawnEntity(spawnLoc, area);
                mobTracker.trackMob(entity, area.getName(), spawnLoc);
                spawned++;
            }
        }

        plugin.getLogger().info(String.format("Initial spawn complete - Spawned %d/%d mobs in area %s", 
            spawned, neededMobs, area.getName()));
    }

    private Entity spawnEntity(Location location, SpawnArea area) {
        Entity entity = location.getWorld().spawnEntity(location, area.getMobType());
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            MobStats stats = area.getMobStats();
            
            if (stats.isShowName()) {
                livingEntity.setCustomName(stats.getDisplayName());
                livingEntity.setCustomNameVisible(true);
            }
            
            livingEntity.setMaxHealth(stats.getHealth());
            livingEntity.setHealth(stats.getHealth());
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
    }

    public boolean toggleSpawning(SpawnArea area) {
        area.setEnabled(!area.isEnabled());
        
        if (area.isEnabled()) {
            startSpawning(area);
        } else {
            stopSpawning(area.getName());
            mobTracker.despawnAreaMobs(area.getName());
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