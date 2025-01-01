package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.CustomDrops;
import com.xmobgeneration.models.MobEquipment;
import com.xmobgeneration.models.MobStats;
import com.xmobgeneration.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaManager {
    private final XMobGeneration plugin;
    private final Map<String, SpawnArea> spawnAreas;
    private final File areasFile;
    private FileConfiguration areasConfig;
    private static final int MAX_AREAS = 45;

    public AreaManager(XMobGeneration plugin) {
        this.plugin = plugin;
        this.spawnAreas = new HashMap<>();
        this.areasFile = new File(plugin.getDataFolder(), "areas.yml");
        loadAreas();
    }

    public void createArea(String name, Location pos1, Location pos2) {
        if (spawnAreas.size() >= MAX_AREAS) {
            throw new IllegalStateException("Maximum number of areas reached (45)");
        }
        SpawnArea area = new SpawnArea(name, pos1, pos2);
        spawnAreas.put(name, area);
        saveAreas();
        plugin.getSpawnManager().startSpawning(area);
    }

    public void deleteArea(String name) {
        SpawnArea area = spawnAreas.remove(name);
        if (area != null) {
            plugin.getSpawnManager().stopSpawning(name);
            saveAreas();
        }
    }

    public SpawnArea getArea(String name) {
        return spawnAreas.get(name);
    }

    public Map<String, SpawnArea> getAllAreas() {
        return new HashMap<>(spawnAreas);
    }

    public boolean hasReachedMaxAreas() {
        return spawnAreas.size() >= MAX_AREAS;
    }

    public int getMaxAreas() {
        return MAX_AREAS;
    }

    public void loadAreas() {
        if (!areasFile.exists()) {
            plugin.saveResource("areas.yml", false);
        }
        areasConfig = YamlConfiguration.loadConfiguration(areasFile);
        
        ConfigurationSection areasSection = areasConfig.getConfigurationSection("areas");
        if (areasSection == null) return;

        for (String name : areasSection.getKeys(false)) {
            ConfigurationSection areaSection = areasSection.getConfigurationSection(name);
            if (areaSection == null) continue;

            Location pos1 = LocationSerializer.deserialize(areaSection.getString("pos1"));
            Location pos2 = LocationSerializer.deserialize(areaSection.getString("pos2"));
            
            if (pos1 == null || pos2 == null) continue;

            SpawnArea area = new SpawnArea(name, pos1, pos2);
            
            // Load mob type (either vanilla or MythicMob)
            if (areaSection.getBoolean("isMythicMob", false)) {
                area.setMythicMob(true);
                area.setMythicMobType(areaSection.getString("mythicMobType", ""));
            } else {
                area.setMythicMob(false);
                area.setMobType(EntityType.valueOf(areaSection.getString("mobType", "ZOMBIE")));
            }

            area.setBossArea(areaSection.getBoolean("isBossArea", false));
            area.setBossSpawnPoint(LocationSerializer.deserialize(areaSection.getString("bossSpawnPoint")));

            area.setSpawnCount(areaSection.getInt("spawnCount", 5));
            area.setRespawnDelay(areaSection.getInt("respawnDelay", 30));
            area.setEnabled(areaSection.getBoolean("enabled", true));

            // Load custom drops
            ConfigurationSection customDropsSection = areaSection.getConfigurationSection("customDrops");
            if (customDropsSection != null) {
                CustomDrops customDrops = new CustomDrops();
                customDrops.setEnabled(customDropsSection.getBoolean("enabled", false));
                
                @SuppressWarnings("unchecked")
                List<ItemStack> items = (List<ItemStack>) customDropsSection.getList("items");
                @SuppressWarnings("unchecked")
                List<Double> chances = (List<Double>) customDropsSection.getList("chances");
                
                if (items != null && chances != null) {
                    customDrops.setItems(items, chances);
                }
                
                area.setCustomDrops(customDrops);
            }

            // Load mob stats
            ConfigurationSection mobStatsSection = areaSection.getConfigurationSection("mobStats");
            if (mobStatsSection != null) {
                MobStats mobStats = new MobStats();
                mobStats.setMobName(mobStatsSection.getString("mobName", "Monster"));
                mobStats.setHealth(mobStatsSection.getDouble("health", 20.0));
                mobStats.setDamage(mobStatsSection.getDouble("damage", 2.0));
                mobStats.setLevel(mobStatsSection.getInt("level", 1));
                mobStats.setShowName(mobStatsSection.getBoolean("showName", false));
                area.setMobStats(mobStats);
            }

            // Load equipment
            ConfigurationSection equipmentSection = areaSection.getConfigurationSection("equipment");
            if (equipmentSection != null) {
                MobEquipment equipment = new MobEquipment();
                equipment.setHelmet((ItemStack) equipmentSection.get("helmet"));
                equipment.setChestplate((ItemStack) equipmentSection.get("chestplate"));
                equipment.setLeggings((ItemStack) equipmentSection.get("leggings"));
                equipment.setBoots((ItemStack) equipmentSection.get("boots"));
                equipment.setOffHand((ItemStack) equipmentSection.get("offHand"));
                area.setMobEquipment(equipment);
            }

            spawnAreas.put(name, area);
        }
    }

    public void initializeSpawning() {
        for (SpawnArea area : spawnAreas.values()) {
            if (area.isEnabled()) {
                plugin.getSpawnManager().startSpawning(area);
            }
        }
    }

    public void saveAreas() {
        areasConfig = new YamlConfiguration();
        ConfigurationSection areasSection = areasConfig.createSection("areas");

        for (Map.Entry<String, SpawnArea> entry : spawnAreas.entrySet()) {
            SpawnArea area = entry.getValue();
            ConfigurationSection areaSection = areasSection.createSection(entry.getKey());
            
            areaSection.set("pos1", LocationSerializer.serialize(area.getPos1()));
            areaSection.set("pos2", LocationSerializer.serialize(area.getPos2()));
            
            // Save mob type (either vanilla or MythicMob)
            areaSection.set("isMythicMob", area.isMythicMob());
            if (area.isMythicMob()) {
                areaSection.set("mythicMobType", area.getMythicMobType());
            } else {
                areaSection.set("mobType", area.getMobType().name());
            }

            areaSection.set("spawnCount", area.getSpawnCount());
            areaSection.set("respawnDelay", area.getRespawnDelay());
            areaSection.set("enabled", area.isEnabled());

            areaSection.set("isBossArea", area.isBossArea());
            areaSection.set("bossSpawnPoint", LocationSerializer.serialize(area.getBossSpawnPoint()));

            // Save custom drops
            ConfigurationSection customDropsSection = areaSection.createSection("customDrops");
            CustomDrops customDrops = area.getCustomDrops();
            customDropsSection.set("enabled", customDrops.isEnabled());
            customDropsSection.set("items", customDrops.getItems());
            customDropsSection.set("chances", customDrops.getChances());

            // Save mob stats
            ConfigurationSection mobStatsSection = areaSection.createSection("mobStats");
            MobStats mobStats = area.getMobStats();
            mobStatsSection.set("mobName", mobStats.getMobName());
            mobStatsSection.set("health", mobStats.getHealth());
            mobStatsSection.set("damage", mobStats.getDamage());
            mobStatsSection.set("level", mobStats.getLevel());
            mobStatsSection.set("showName", mobStats.isShowName());

            // Save equipment
            ConfigurationSection equipmentSection = areaSection.createSection("equipment");
            MobEquipment equipment = area.getMobEquipment();
            equipmentSection.set("helmet", equipment.getHelmet());
            equipmentSection.set("chestplate", equipment.getChestplate());
            equipmentSection.set("leggings", equipment.getLeggings());
            equipmentSection.set("boots", equipment.getBoots());
            equipmentSection.set("offHand", equipment.getOffHand());
        }

        try {
            areasConfig.save(areasFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save areas.yml: " + e.getMessage());
        }
    }
}