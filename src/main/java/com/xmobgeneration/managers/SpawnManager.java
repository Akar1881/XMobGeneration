package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.managers.spawn.LocationFinder;
import com.xmobgeneration.managers.spawn.MobTracker;
import com.xmobgeneration.managers.spawn.RespawnTask;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SpawnManager {
    private final XMobGeneration plugin;
    private final Map<String, BukkitRunnable> spawnTasks = new HashMap<>();
    private final MobTracker mobTracker;
    private final LocationFinder locationFinder;
    private final RespawnTask respawnTask;

    public SpawnManager(XMobGeneration plugin) {
        this.plugin = plugin;
        this.mobTracker = new MobTracker();
        this.locationFinder = new LocationFinder();
        this.respawnTask = new RespawnTask(plugin, mobTracker, locationFinder);
        
        startRespawnTask();
    }

    private void startRespawnTask() {
        respawnTask.runTaskTimer(plugin, 20L, 20L); // Check every second
    }

    public void startSpawning(SpawnArea area) {
        stopSpawning(area.getName());
        
        if (!area.isEnabled()) {
            return;
        }

        plugin.getLogger().info("Starting initial spawn for area: " + area.getName());
        performInitialSpawn(area);
    }

    public void restartArea(SpawnArea area) {
        // First, despawn all existing mobs in this area
        mobTracker.despawnAreaMobs(area.getName());
        
        // Then start spawning new mobs
        startSpawning(area);
    }

    private void performInitialSpawn(SpawnArea area) {
        int attempts = 0;
        int maxAttempts = area.getSpawnCount() * 3;
        int spawned = 0;

        while (spawned < area.getSpawnCount() && attempts < maxAttempts) {
            attempts++;
            Location spawnLoc = locationFinder.findSafeSpawnLocation(area);
            
            if (spawnLoc != null) {
                Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, area.getMobType());
                mobTracker.trackMob(entity, area.getName(), spawnLoc);
                spawned++;
            }
        }

        plugin.getLogger().info(String.format("Initial spawn complete - Spawned %d/%d mobs in area %s", 
            spawned, area.getSpawnCount(), area.getName()));
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

    public MobTracker getMobTracker() {
        return mobTracker;
    }
}