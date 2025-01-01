package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.managers.spawn.*;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.MobEquipment;
import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnManager {
    private final XMobGeneration plugin;
    private final Map<String, BukkitRunnable> spawnTasks = new HashMap<>();
    private final MobTracker mobTracker;
    private final LocationFinder locationFinder;
    private final BossSpawnHandler bossSpawnHandler;
    private RespawnTask respawnTask;

    public SpawnManager(XMobGeneration plugin) {
        this.plugin = plugin;
        this.mobTracker = new MobTracker();
        this.locationFinder = new LocationFinder();
        this.bossSpawnHandler = new BossSpawnHandler(plugin);
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

        mobTracker.despawnAreaMobs(area.getName());
        
        if (area.isBossArea()) {
            bossSpawnHandler.removeBossTracking(area.getName());
            if (area.isEnabled()) {
                bossSpawnHandler.spawnBoss(area);
            }
        } else {
            performInitialSpawn(area);
        }
    }

    private void performInitialSpawn(SpawnArea area) {
        if (area.isBossArea()) {
            return;
        }

        int neededMobs = area.getSpawnCount();
        int attempts = 0;
        int maxAttempts = neededMobs * 3;
        int spawned = 0;

        while (spawned < neededMobs && attempts < maxAttempts) {
            attempts++;
            Location spawnLoc = locationFinder.findSafeSpawnLocation(area);
            
            if (spawnLoc != null) {
                Entity entity = spawnEntity(spawnLoc, area);
                if (entity != null) {
                    mobTracker.trackMob(entity, area.getName(), spawnLoc);
                    spawned++;
                }
            }
        }
    }

    private Entity spawnEntity(Location location, SpawnArea area) {
        Entity entity;
        
        if (area.isMythicMob()) {
            entity = plugin.getMythicMobsManager().spawnMythicMob(
                area.getMythicMobType(), 
                location,
                area.getMobStats().getLevel()
            );
            
            if (entity == null) {
                plugin.getLogger().warning("Failed to spawn MythicMob: " + area.getMythicMobType());
                return null;
            }
        } else {
            entity = location.getWorld().spawnEntity(location, area.getMobType());
        }

        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            
            if (!area.isMythicMob()) {
                if (area.getMobStats().isShowName()) {
                    livingEntity.setCustomName(area.getMobStats().getDisplayName());
                    livingEntity.setCustomNameVisible(true);
                }
                
                livingEntity.setMaxHealth(area.getMobStats().getHealth());
                livingEntity.setHealth(area.getMobStats().getHealth());
            }

            livingEntity.setMetadata("mobDamage", new FixedMetadataValue(plugin, area.getMobStats().getDamage()));
            applyEquipment(livingEntity, area.getMobEquipment());
        }
        
        return entity;
    }

    private void applyEquipment(LivingEntity entity, MobEquipment mobEquipment) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment != null) {
            equipment.setHelmet(mobEquipment.getHelmet());
            equipment.setChestplate(mobEquipment.getChestplate());
            equipment.setLeggings(mobEquipment.getLeggings());
            equipment.setBoots(mobEquipment.getBoots());
            equipment.setItemInOffHand(mobEquipment.getOffHand());

            equipment.setHelmetDropChance(0);
            equipment.setChestplateDropChance(0);
            equipment.setLeggingsDropChance(0);
            equipment.setBootsDropChance(0);
            equipment.setItemInOffHandDropChance(0);
        }
    }

    public void handleMobDeath(Entity entity) {
        SpawnedMob mob = mobTracker.getMob(entity.getUniqueId());
        if (mob != null) {
            mobTracker.handleMobDeath(entity);
            
            if (entity.hasMetadata("isBoss")) {
                String areaName = entity.getMetadata("areaName").get(0).asString();
                bossSpawnHandler.handleBossDeath(areaName, entity.getUniqueId());
            }
        }
    }

    public void stopSpawning(String areaName) {
        BukkitRunnable task = spawnTasks.remove(areaName);
        if (task != null) {
            task.cancel();
        }
        bossSpawnHandler.cancelRespawnTask(areaName);
    }

    public void despawnBossAreaMobs(String areaName) {
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        if (area != null && area.isBossArea()) {
            mobTracker.despawnAreaMobs(areaName);
        }
    }

    public boolean toggleSpawning(SpawnArea area) {
        area.setEnabled(!area.isEnabled());
        
        if (area.isEnabled()) {
            startSpawning(area);
        } else {
            stopSpawning(area.getName());
            mobTracker.despawnAreaMobs(area.getName());
            if (area.isBossArea()) {
                bossSpawnHandler.removeBossTracking(area.getName());
            }
        }
        
        return area.isEnabled();
    }

    public void restartArea(SpawnArea area) {
        stopSpawning(area.getName());
        mobTracker.despawnAreaMobs(area.getName());
        
        if (area.isBossArea()) {
            bossSpawnHandler.removeBossTracking(area.getName());
        }
        
        if (area.isEnabled()) {
            startSpawning(area);
        }
    }

    public MobTracker getMobTracker() {
        return mobTracker;
    }

    public UUID getBossUUID(String areaName) {
        return bossSpawnHandler.getBossUUID(areaName);
    }
}