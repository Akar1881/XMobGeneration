package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AreaManager {
    private final XMobGeneration plugin;
    private final Map<String, SpawnArea> spawnAreas;
    private final File areasFile;
    private FileConfiguration areasConfig;
    private static final int MAX_AREAS = 45; // Added maximum area limit

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
            area.setMobType(EntityType.valueOf(areaSection.getString("mobType", "ZOMBIE")));
            area.setSpawnCount(areaSection.getInt("spawnCount", 5));
            area.setRespawnDelay(areaSection.getInt("respawnDelay", 30));
            area.setEnabled(areaSection.getBoolean("enabled", true));

            spawnAreas.put(name, area);
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
            areaSection.set("mobType", area.getMobType().name());
            areaSection.set("spawnCount", area.getSpawnCount());
            areaSection.set("respawnDelay", area.getRespawnDelay());
            areaSection.set("enabled", area.isEnabled());
        }

        try {
            areasConfig.save(areasFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save areas.yml: " + e.getMessage());
        }
    }
}