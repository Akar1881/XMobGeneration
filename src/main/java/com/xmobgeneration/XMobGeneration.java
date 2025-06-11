package com.xmobgeneration;

import com.xmobgeneration.commands.CommandManager;
import com.xmobgeneration.config.ConfigManager;
import com.xmobgeneration.gui.GUIManager;
import com.xmobgeneration.listeners.*;
import com.xmobgeneration.managers.AreaManager;
import com.xmobgeneration.managers.SpawnManager;
import com.xmobgeneration.managers.XPManager;
import com.xmobgeneration.models.BossDamageTracker;
import com.xmobgeneration.managers.RestartManager;
import com.xmobgeneration.mythicmobs.MythicMobsManager;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class XMobGeneration extends JavaPlugin {
    private static XMobGeneration instance;
    private ConfigManager configManager;
    private AreaManager areaManager;
    private SpawnManager spawnManager;
    private GUIManager guiManager;
    private RestartManager restartManager;
    private MythicMobsManager mythicMobsManager;
    private BossDamageTracker bossDamageTracker;
    private XPManager xpManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers in correct order
        this.configManager = new ConfigManager(this);
        this.mythicMobsManager = new MythicMobsManager(this);
        this.spawnManager = new SpawnManager(this);
        this.areaManager = new AreaManager(this);
        this.guiManager = new GUIManager(this);
        this.restartManager = new RestartManager(this);
        this.bossDamageTracker = new BossDamageTracker();
        this.xpManager = new XPManager(this);

        // Register commands and listeners
        getCommand("xmg").setExecutor(new CommandManager(this));
        registerListeners();

        // Initialize spawning after all areas are loaded
        getServer().getScheduler().runTaskLater(this, () -> {
            areaManager.initializeSpawning();
        }, 20L);

        if (mythicMobsManager.isMythicMobsEnabled()) {
            getLogger().info("MythicMobs support enabled!");
        }

        getLogger().info("XMobGeneration has been enabled!");
    }

    @Override
    public void onDisable() {
        // Cancel all scheduled tasks
        if (restartManager != null) {
            restartManager.stop();
        }
        
        // Stop all spawn tasks and despawn mobs
        for (SpawnArea area : areaManager.getAllAreas().values()) {
            spawnManager.stopSpawning(area.getName());
            spawnManager.getMobTracker().despawnAreaMobs(area.getName());
        }
        
        // Save all areas
        areaManager.saveAreas();
        
        // Clear all boss tracking
        for (SpawnArea area : areaManager.getAllAreas().values()) {
            if (area.isBossArea()) {
                spawnManager.getBossSpawnHandler().removeBossTracking(area.getName());
            }
        }
        
        // Cancel all Bukkit tasks
        Bukkit.getScheduler().cancelTasks(this);
        
        // Clear all managers
        spawnManager = null;
        areaManager = null;
        restartManager = null;
        configManager = null;
        guiManager = null;
        mythicMobsManager = null;

        getLogger().info("XMobGeneration has been disabled!");
    }

    private void registerListeners() {
        // Register all event listeners
        getServer().getPluginManager().registerEvents(new MobDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new MobSpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new MobContainmentListener(this), this);
        getServer().getPluginManager().registerEvents(new BossWandListener(this), this);
        getServer().getPluginManager().registerEvents(new AreaSelectionListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomDropsMenuListener(this), this);
    }

    public static XMobGeneration getInstance() {
        return instance;
    }

    public BossDamageTracker getBossDamageTracker() {
        return bossDamageTracker;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public XPManager getXPManager() {
        return xpManager;
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

    public MythicMobsManager getMythicMobsManager() {
        return mythicMobsManager;
    }
}