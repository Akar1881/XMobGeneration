package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.SpawnedMob;
import com.xmobgeneration.models.MobEquipment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RespawnTask extends BukkitRunnable {
    private final XMobGeneration plugin;
    private final MobTracker mobTracker;
    private final LocationFinder locationFinder;

    public RespawnTask(XMobGeneration plugin, MobTracker mobTracker, LocationFinder locationFinder) {
        this.plugin = plugin;
        this.mobTracker = mobTracker;
        this.locationFinder = locationFinder;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        
        for (SpawnArea area : plugin.getAreaManager().getAllAreas().values()) {
            if (!area.isEnabled()) continue;
            
            // Handle boss respawn separately
            if (area.isBossArea()) {
                handleBossRespawn(area, currentTime);
                continue;
            }
            
            List<SpawnedMob> mobsToRespawn = mobTracker.getDeadMobsReadyToRespawn(currentTime, area.getRespawnDelay());
            for (SpawnedMob mob : mobsToRespawn) {
                respawnMob(mob, area);
            }
        }
    }

    private void handleBossRespawn(SpawnArea area, long currentTime) {
        if (plugin.getSpawnManager().getBossUUID(area.getName()) == null) {
            List<SpawnedMob> deadBosses = mobTracker.getDeadMobsReadyToRespawn(currentTime, area.getRespawnDelay());
            if (!deadBosses.isEmpty()) {
                respawnBoss(deadBosses.get(0), area);
            }
        }
    }

    private void respawnBoss(SpawnedMob mob, SpawnArea area) {
        if (area.getBossSpawnPoint() == null) return;
        
        Location spawnLoc = area.getBossSpawnPoint().clone().add(0.5, 0, 0.5);
        mobTracker.removeTrackedMob(mob);
        
        Entity entity = spawnBossEntity(spawnLoc, area);
        if (entity != null) {
            mobTracker.trackMob(entity, area.getName(), spawnLoc);
        }
    }

    private Entity spawnBossEntity(Location location, SpawnArea area) {
        Entity entity;
        
        if (area.isMythicMob()) {
            entity = plugin.getMythicMobsManager().spawnMythicMob(
                area.getMythicMobType(),
                location,
                area.getMobStats().getLevel()
            );
        } else {
            entity = location.getWorld().spawnEntity(location, area.getMobType());
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
            
            applyEquipment(livingEntity, area.getMobEquipment());
        }
        
        return entity;
    }

    private void respawnMob(SpawnedMob mob, SpawnArea area) {
        Location spawnLoc = locationFinder.findSafeSpawnLocation(area);
        if (spawnLoc == null) return;

        mobTracker.removeTrackedMob(mob);
        
        Entity entity;
        
        if (area.isMythicMob()) {
            entity = plugin.getMythicMobsManager().spawnMythicMob(
                area.getMythicMobType(),
                spawnLoc,
                area.getMobStats().getLevel()
            );
        } else {
            entity = spawnLoc.getWorld().spawnEntity(spawnLoc, area.getMobType());
            
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                
                if (area.getMobStats().isShowName()) {
                    livingEntity.setCustomName(area.getMobStats().getDisplayName());
                    livingEntity.setCustomNameVisible(true);
                }
                
                livingEntity.setMaxHealth(area.getMobStats().getHealth());
                livingEntity.setHealth(area.getMobStats().getHealth());
                livingEntity.setMetadata("mobDamage", new FixedMetadataValue(plugin, area.getMobStats().getDamage()));
                
                applyEquipment(livingEntity, area.getMobEquipment());
            }
        }
        
        if (entity != null) {
            mobTracker.trackMob(entity, area.getName(), spawnLoc);
        }
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
}