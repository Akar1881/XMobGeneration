package com.xmobgeneration;

import com.xmobgeneration.commands.CommandManager;
import com.xmobgeneration.config.ConfigManager;
import com.xmobgeneration.gui.GUIManager;
import com.xmobgeneration.listeners.GUIListener;
import com.xmobgeneration.listeners.MobDeathListener;
import com.xmobgeneration.managers.AreaManager;
import com.xmobgeneration.managers.SpawnManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XMobGeneration extends JavaPlugin {
    private static XMobGeneration instance;
    private ConfigManager configManager;
    private AreaManager areaManager;
    private SpawnManager spawnManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.spawnManager = new SpawnManager(this);
        this.areaManager = new AreaManager(this);
        this.guiManager = new GUIManager(this);

        // Register commands and listeners
        getCommand("xmg").setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(this), this);

        getLogger().info("XMobGeneration has been enabled!");
    }

    @Override
    public void onDisable() {
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