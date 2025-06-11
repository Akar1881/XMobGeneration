package com.xmobgeneration.models;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SpawnArea {
    private String name;
    private Location pos1;
    private Location pos2;
    private EntityType mobType;
    private String mythicMobType; // Add this field
    private boolean isMythicMob; // Add this field
    private int spawnCount;
    private int respawnDelay;
    private boolean enabled;
    private CustomDrops customDrops;
    private MobStats mobStats;
    private MobEquipment mobEquipment;
    private boolean isBossArea;
    private Location bossSpawnPoint;
    private int xpAmount = 10; // Default XP amount
    private int minLevel = 1; // Default min level
    private int maxLevel = 1; // Default max level
    private boolean playerProximityRequired = false; // Whether spawning requires player proximity
    private int proximityRange = 50; // Default proximity range in blocks

    public SpawnArea(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.mobType = EntityType.ZOMBIE;
        this.mythicMobType = "";
        this.isMythicMob = false;
        this.spawnCount = 5;
        this.respawnDelay = 30;
        this.enabled = true;
        this.customDrops = new CustomDrops();
        this.mobStats = new MobStats();
        this.mobEquipment = new MobEquipment();
    }

    // Add getters and setters for new fields
    public String getMythicMobType() {
        return mythicMobType;
    }

    public void setMythicMobType(String mythicMobType) {
        this.mythicMobType = mythicMobType;
    }

    public boolean isMythicMob() {
        return isMythicMob;
    }

    public void setMythicMob(boolean mythicMob) {
        isMythicMob = mythicMob;
    }

    // Existing getters and setters remain unchanged
    public String getName() {
        return name;
    }

    public int getXpAmount() {
        return xpAmount;
    }

    public void setXpAmount(int xpAmount) {
        this.xpAmount = Math.max(0, xpAmount);
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
    public boolean isBossArea() {
        return isBossArea;
    }
    
    public void setBossArea(boolean bossArea) {
        isBossArea = bossArea;
    }
    
    public Location getBossSpawnPoint() {
        return bossSpawnPoint;
    }
    
    public void setBossSpawnPoint(Location bossSpawnPoint) {
        this.bossSpawnPoint = bossSpawnPoint;
    }
    
    public int getMinLevel() {
        return minLevel;
    }
    
    public void setMinLevel(int minLevel) {
        // Ensure min level is between 1 and 1000
        this.minLevel = Math.min(1000, Math.max(1, minLevel));
        // Ensure max level is not less than min level
        this.maxLevel = Math.max(this.minLevel, this.maxLevel);
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public void setMaxLevel(int maxLevel) {
        // Ensure max level is between min level and 1000
        this.maxLevel = Math.min(1000, Math.max(this.minLevel, maxLevel));
    }
    
    public boolean isPlayerProximityRequired() {
        return playerProximityRequired;
    }
    
    public void setPlayerProximityRequired(boolean playerProximityRequired) {
        this.playerProximityRequired = playerProximityRequired;
    }
    
    public int getProximityRange() {
        return proximityRange;
    }
    
    public void setProximityRange(int proximityRange) {
        this.proximityRange = Math.max(10, proximityRange);
    }
    
    /**
     * Gets a random level between minLevel and maxLevel
     * @return A random level value
     */
    public int getRandomLevel() {
        if (minLevel == maxLevel) {
            return minLevel;
        }
        // Use a safer random calculation to prevent integer overflow
        return minLevel + (int)(Math.random() * (maxLevel - minLevel + 1));
    }
}