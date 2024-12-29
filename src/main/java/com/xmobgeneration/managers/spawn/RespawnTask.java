package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
        if (spawnLoc != null) {
            // Remove old mob data
            mobTracker.removeTrackedMob(mob);
            
            // Spawn new mob
            Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, area.getMobType());
            
            // Apply mob stats if it's a living entity
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
            }
            
            // Track the new mob
            mobTracker.trackMob(entity, area.getName(), spawnLoc);
            
            plugin.getLogger().info("Respawned mob in area: " + area.getName() + " at " + 
                String.format("(%.1f, %.1f, %.1f)", 
                    spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ()));
        }
    }
}