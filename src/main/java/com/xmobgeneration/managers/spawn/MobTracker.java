package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class MobTracker {
    private final Map<UUID, SpawnedMob> trackedMobs = new HashMap<>();
    private final Map<String, Set<SpawnedMob>> areaSpawnedMobs = new HashMap<>();
    private static final long CLEANUP_INTERVAL = 300000L; // 5 minutes in milliseconds
    private long lastCleanup = System.currentTimeMillis();

    public void trackMob(Entity entity, String areaName, Location spawnLoc) {
        SpawnedMob trackedMob = new SpawnedMob(entity, areaName, spawnLoc);
        trackedMobs.put(entity.getUniqueId(), trackedMob);
        areaSpawnedMobs.computeIfAbsent(areaName, k -> new HashSet<>()).add(trackedMob);
        
        // Perform cleanup if needed
        performCleanupIfNeeded();
    }

    private void performCleanupIfNeeded() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCleanup >= CLEANUP_INTERVAL) {
            cleanupDeadMobs();
            lastCleanup = currentTime;
        }
    }

    private void cleanupDeadMobs() {
        // Remove dead mobs that have been dead for more than 1 hour
        long oneHourAgo = System.currentTimeMillis() - 3600000L;
        
        trackedMobs.entrySet().removeIf(entry -> {
            SpawnedMob mob = entry.getValue();
            if (mob.isDead() && mob.getDeathTime() < oneHourAgo) {
                Set<SpawnedMob> areaMobs = areaSpawnedMobs.get(mob.getAreaName());
                if (areaMobs != null) {
                    areaMobs.remove(mob);
                    if (areaMobs.isEmpty()) {
                        areaSpawnedMobs.remove(mob.getAreaName());
                    }
                }
                return true;
            }
            return false;
        });
    }

    public void handleMobDeath(Entity entity) {
        SpawnedMob mob = trackedMobs.get(entity.getUniqueId());
        if (mob != null) {
            mob.setDead(true);
            mob.setDeathTime(System.currentTimeMillis());
        }
    }

    public void removeTrackedMob(SpawnedMob mob) {
        UUID entityId = mob.getEntityId();
        trackedMobs.remove(entityId);
        
        Set<SpawnedMob> areaMobs = areaSpawnedMobs.get(mob.getAreaName());
        if (areaMobs != null) {
            areaMobs.remove(mob);
            if (areaMobs.isEmpty()) {
                areaSpawnedMobs.remove(mob.getAreaName());
            }
        }
    }

    public int getAreaMobCount(String areaName) {
        Set<SpawnedMob> areaMobs = areaSpawnedMobs.get(areaName);
        if (areaMobs == null) {
            return 0;
        }
        
        return (int) areaMobs.stream()
            .filter(mob -> !mob.isDead() && mob.getEntity() != null && !mob.getEntity().isDead())
            .count();
    }

    public void clearAreaMobs(String areaName) {
        Set<SpawnedMob> areaMobs = areaSpawnedMobs.remove(areaName);
        if (areaMobs != null) {
            for (SpawnedMob mob : new HashSet<>(areaMobs)) {
                Entity entity = mob.getEntity();
                if (entity != null && !entity.isDead()) {
                    entity.remove();
                }
                trackedMobs.remove(mob.getEntityId());
            }
        }
    }

    public void despawnAreaMobs(String areaName) {
        Set<SpawnedMob> areaMobs = areaSpawnedMobs.get(areaName);
        if (areaMobs != null) {
            // Create a new HashSet to avoid ConcurrentModificationException
            for (SpawnedMob mob : new HashSet<>(areaMobs)) {
                Entity entity = mob.getEntity();
                if (entity != null && !entity.isDead()) {
                    entity.remove();
                }
                removeTrackedMob(mob);
            }
            // Clear the area's mob set
            areaSpawnedMobs.remove(areaName);
        }
    }

    public List<SpawnedMob> getDeadMobsReadyToRespawn(long currentTime, int respawnDelay) {
        List<SpawnedMob> mobsToRespawn = new ArrayList<>();
        
        for (Set<SpawnedMob> areaMobs : areaSpawnedMobs.values()) {
            for (SpawnedMob mob : areaMobs) {
                if (mob.isDead()) {
                    long respawnTime = mob.getDeathTime() + (respawnDelay * 1000L);
                    if (currentTime >= respawnTime) {
                        mobsToRespawn.add(mob);
                    }
                }
            }
        }
        
        return mobsToRespawn;
    }

    public SpawnedMob getMob(UUID entityId) {
        return trackedMobs.get(entityId);
    }

    public Collection<SpawnedMob> getAllMobs() {
        return Collections.unmodifiableCollection(trackedMobs.values());
    }
}