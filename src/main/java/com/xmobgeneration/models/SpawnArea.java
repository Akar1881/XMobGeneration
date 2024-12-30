package com.xmobgeneration.models;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SpawnArea {
    private String name;
    private Location pos1;
    private Location pos2;
    private EntityType mobType;
    private int spawnCount;
    private int respawnDelay;
    private boolean enabled;
    private CustomDrops customDrops;
    private MobStats mobStats;
    private MobEquipment mobEquipment;

    public SpawnArea(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.mobType = EntityType.ZOMBIE;
        this.spawnCount = 5;
        this.respawnDelay = 30;
        this.enabled = true;
        this.customDrops = new CustomDrops();
        this.mobStats = new MobStats();
        this.mobEquipment = new MobEquipment();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public EntityType getMobType() {
        return mobType;
    }

    public void setMobType(EntityType mobType) {
        this.mobType = mobType;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public void setSpawnCount(int spawnCount) {
        this.spawnCount = spawnCount;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public void setRespawnDelay(int respawnDelay) {
        this.respawnDelay = respawnDelay;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CustomDrops getCustomDrops() {
        return customDrops;
    }

    public void setCustomDrops(CustomDrops customDrops) {
        this.customDrops = customDrops;
    }

    public MobStats getMobStats() {
        return mobStats;
    }

    public void setMobStats(MobStats mobStats) {
        this.mobStats = mobStats;
    }

    public MobEquipment getMobEquipment() {
        return mobEquipment;
    }

    public void setMobEquipment(MobEquipment mobEquipment) {
        this.mobEquipment = mobEquipment;
    }
}