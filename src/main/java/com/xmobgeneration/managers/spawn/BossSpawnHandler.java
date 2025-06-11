package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BossSpawnHandler {
    private final XMobGeneration plugin;
    private final Map<String, BukkitRunnable> respawnTasks = new HashMap<>();
    private final Map<String, UUID> bossUUIDs = new HashMap<>();
    private final Map<String, Boolean> isRespawning = new HashMap<>();
    private final Lock spawnLock = new ReentrantLock();
    private final Lock respawnLock = new ReentrantLock();

    public BossSpawnHandler(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void handleBossDeath(String areaName, UUID bossId) {
        spawnLock.lock();
        try {
            if (!bossUUIDs.containsKey(areaName) || !bossUUIDs.get(areaName).equals(bossId)) {
                return; // Not the current boss or already handled
            }

            bossUUIDs.remove(areaName);
            cancelRespawnTask(areaName);
            
            SpawnArea area = plugin.getAreaManager().getArea(areaName);
            if (area != null && area.isEnabled() && !isRespawning.getOrDefault(areaName, false)) {
                // Clear any external mobs in the area
                plugin.getSpawnManager().despawnBossAreaMobs(areaName);
                // Schedule boss respawn
                scheduleBossRespawn(area);
            }
        } finally {
            spawnLock.unlock();
        }
    }

    private void scheduleBossRespawn(SpawnArea area) {
        respawnLock.lock();
        try {
            cancelRespawnTask(area.getName());
            isRespawning.put(area.getName(), true);

            BukkitRunnable respawnTask = new BukkitRunnable() {
                @Override
                public void run() {
                    spawnLock.lock();
                    try {
                        if (area.isEnabled() && !hasBoss(area.getName())) {
                            spawnBoss(area);
                        }
                        isRespawning.remove(area.getName());
                    } finally {
                        spawnLock.unlock();
                    }
                }
            };

            respawnTask.runTaskLater(plugin, area.getRespawnDelay() * 20L);
            respawnTasks.put(area.getName(), respawnTask);
        } finally {
            respawnLock.unlock();
        }
    }

    public void spawnBoss(SpawnArea area) {
        spawnLock.lock();
        try {
            if (!area.isBossArea() || area.getBossSpawnPoint() == null || hasBoss(area.getName())) {
                return;
            }

            Location spawnLoc = area.getBossSpawnPoint().clone().add(0.5, 0, 0.5);
            Entity entity;

            if (area.isMythicMob()) {
                entity = plugin.getMythicMobsManager().spawnMythicMob(
                    area.getMythicMobType(),
                    spawnLoc,
                    area.getMobStats().getLevel()
                );
            } else {
                entity = spawnLoc.getWorld().spawnEntity(spawnLoc, area.getMobType());
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                
                if (!area.isMythicMob()) {
                    livingEntity.setMaxHealth(area.getMobStats().getHealth());
                    livingEntity.setHealth(area.getMobStats().getHealth());
                    livingEntity.setCustomName(area.getMobStats().getDisplayName());
                    livingEntity.setCustomNameVisible(true);
                }
                
                livingEntity.setMetadata("isBoss", new FixedMetadataValue(plugin, true));
                livingEntity.setMetadata("areaName", new FixedMetadataValue(plugin, area.getName()));
                livingEntity.setMetadata("mobDamage", new FixedMetadataValue(plugin, area.getMobStats().getDamage()));
                
                plugin.getSpawnManager().getMobTracker().trackMob(entity, area.getName(), spawnLoc);
                bossUUIDs.put(area.getName(), entity.getUniqueId());
            }
        } finally {
            spawnLock.unlock();
        }
    }

    public void cancelRespawnTask(String areaName) {
        respawnLock.lock();
        try {
            BukkitRunnable task = respawnTasks.remove(areaName);
            if (task != null) {
                task.cancel();
            }
            isRespawning.remove(areaName);
        } finally {
            respawnLock.unlock();
        }
    }

    public boolean hasBoss(String areaName) {
        spawnLock.lock();
        try {
            return bossUUIDs.containsKey(areaName);
        } finally {
            spawnLock.unlock();
        }
    }

    public UUID getBossUUID(String areaName) {
        spawnLock.lock();
        try {
            return bossUUIDs.get(areaName);
        } finally {
            spawnLock.unlock();
        }
    }

    public void removeBossTracking(String areaName) {
        spawnLock.lock();
        try {
            bossUUIDs.remove(areaName);
            cancelRespawnTask(areaName);
            isRespawning.remove(areaName);
        } finally {
            spawnLock.unlock();
        }
    }
}