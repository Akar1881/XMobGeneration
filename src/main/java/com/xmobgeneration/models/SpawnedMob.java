package com.xmobgeneration.models;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import java.util.UUID;

public class SpawnedMob {
    private final UUID entityId;
    private final String areaName;
    private final Location spawnLocation;
    private long deathTime;
    private boolean isDead;

    public SpawnedMob(Entity entity, String areaName, Location spawnLocation) {
        this.entityId = entity.getUniqueId();
        this.areaName = areaName;
        this.spawnLocation = spawnLocation;
        this.isDead = false;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public String getAreaName() {
        return areaName;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public long getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Entity getEntity() {
        return Bukkit.getEntity(entityId);
    }
}