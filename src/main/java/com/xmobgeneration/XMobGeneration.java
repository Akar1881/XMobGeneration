package com.xmobgeneration;

import com.xmobgeneration.commands.CommandManager;
import com.xmobgeneration.config.ConfigManager;
import com.xmobgeneration.gui.GUIManager;
import com.xmobgeneration.listeners.*;
import com.xmobgeneration.managers.AreaManager;
import com.xmobgeneration.managers.SpawnManager;
import com.xmobgeneration.managers.RestartManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XMobGeneration extends JavaPlugin {
    private static XMobGeneration instance;
    private ConfigManager configManager;
    private AreaManager areaManager;
    private SpawnManager spawnManager;
    private GUIManager guiManager;
    private RestartManager restartManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers in correct order
        this.configManager = new ConfigManager(this);
        this.spawnManager = new SpawnManager(this);
        this.areaManager = new AreaManager(this);
        this.guiManager = new GUIManager(this);
        this.restartManager = new RestartManager(this);

        // Register commands and listeners
        getCommand("xmg").setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomDropsListener(this), this);
        getServer().getPluginManager().registerEvents(new MobDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomDropsMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new MobContainmentListener(this), this);

        // Initialize spawning after all areas are loaded
        getServer().getScheduler().runTaskLater(this, () -> {
            areaManager.initializeSpawning();
        }, 20L); // Wait 1 second to ensure everything is loaded

        getLogger().info("XMobGeneration has been enabled!");
    }

    @Override
    public void onDisable() {
        // Stop the restart manager first
        if (restartManager != null) {
            restartManager.stop();
        }

        // Despawn all mobs from all areas
        if (spawnManager != null) {
            for (String areaName : areaManager.getAllAreas().keySet()) {
                spawnManager.getMobTracker().despawnAreaMobs(areaName);
            }
        }

        // Save areas configuration last
        if (areaManager != null) {
            areaManager.saveAreas();
        }

        getLogger().info("XMobGeneration has been disabled!");
    }

    public static XMobGeneration getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public AreaManager getAreaManager() {
        return areaManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public GUIManager getGUIManager() {
        return guiManager;
    }
}