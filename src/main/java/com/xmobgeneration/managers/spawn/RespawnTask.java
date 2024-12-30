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
            
            List<SpawnedMob> mobsToRespawn = mobTracker.getDeadMobsReadyToRespawn(currentTime, area.getRespawnDelay());
            
            for (SpawnedMob mob : mobsToRespawn) {
                respawnMob(mob, area);
            }
        }
    }

    private void respawnMob(SpawnedMob mob, SpawnArea area) {
        Location spawnLoc = locationFinder.findSafeSpawnLocation(area);
        if (spawnLoc == null) return;

        // Remove old mob data
        mobTracker.removeTrackedMob(mob);
        
        Entity entity;
        
        if (area.isMythicMob()) {
            // Spawn MythicMob
            entity = plugin.getMythicMobsManager().spawnMythicMob(
                area.getMythicMobType(), 
                spawnLoc,
                area.getMobStats().getLevel()
            );
            
            if (entity == null) {
                plugin.getLogger().warning("Failed to respawn MythicMob: " + area.getMythicMobType());
                return;
            }
        } else {
            // Spawn vanilla mob
            entity = spawnLoc.getWorld().spawnEntity(spawnLoc, area.getMobType());
            
            // Apply vanilla mob customizations
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                
                // Set custom name
                if (area.getMobStats().isShowName()) {
                    livingEntity.setCustomName(area.getMobStats().getDisplayName());
                    livingEntity.setCustomNameVisible(true);
                }
                
                // Set health
                livingEntity.setMaxHealth(area.getMobStats().getHealth());
                livingEntity.setHealth(area.getMobStats().getHealth());
                
                // Set damage
                livingEntity.setMetadata("mobDamage", new FixedMetadataValue(plugin, area.getMobStats().getDamage()));

                // Apply equipment
                EntityEquipment equipment = livingEntity.getEquipment();
                if (equipment != null) {
                    MobEquipment mobEquipment = area.getMobEquipment();
                    
                    equipment.setHelmet(mobEquipment.getHelmet());
                    equipment.setChestplate(mobEquipment.getChestplate());
                    equipment.setLeggings(mobEquipment.getLeggings());
                    equipment.setBoots(mobEquipment.getBoots());
                    equipment.setItemInOffHand(mobEquipment.getOffHand());

                    // Set drop chances to 0
                    equipment.setHelmetDropChance(0);
                    equipment.setChestplateDropChance(0);
                    equipment.setLeggingsDropChance(0);
                    equipment.setBootsDropChance(0);
                    equipment.setItemInOffHandDropChance(0);
                }
            }
        }
        
        // Track the new mob
        mobTracker.trackMob(entity, area.getName(), spawnLoc);
        
        plugin.getLogger().info(String.format(
            "Respawned %s in area: %s at (%.1f, %.1f, %.1f)",
            area.isMythicMob() ? "MythicMob " + area.getMythicMobType() : area.getMobType().toString(),
            area.getName(),
            spawnLoc.getX(),
            spawnLoc.getY(),
            spawnLoc.getZ()
        ));
    }
}