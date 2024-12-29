package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RestartManager {
    private final XMobGeneration plugin;
    private BukkitTask restartTask;
    private static final long WARNING_TIME = 1200L; // 1 minute in ticks
    private static final long DEFAULT_RESTART_INTERVAL = 12000L; // 10 minutes in ticks
    private static final long MIN_RESTART_INTERVAL = 1200L; // 1 minute in ticks

    public RestartManager(XMobGeneration plugin) {
        this.plugin = plugin;
        startRestartTask();
    }

    private long getRestartInterval() {
        int configInterval = plugin.getConfigManager().getConfig().getInt("settings.restart-interval", 10);
        
        // Convert minutes to ticks (20 ticks = 1 second)
        long intervalTicks = configInterval * 60L * 20L;
        
        // Ensure interval is greater than 1 minute
        if (intervalTicks <= MIN_RESTART_INTERVAL) {
            plugin.getLogger().warning("Restart interval must be greater than 1 minute! Using default value of 10 minutes.");
            Bukkit.broadcastMessage(plugin.getConfigManager().getMessage("invalid-restart-interval"));
            return DEFAULT_RESTART_INTERVAL;
        }
        
        return intervalTicks;
    }

    private void startRestartTask() {
        final long restartInterval = getRestartInterval();
        
        restartTask = new BukkitRunnable() {
            private long ticksUntilRestart = restartInterval;

            @Override
            public void run() {
                if (ticksUntilRestart <= 0) {
                    restartMobAreas();
                    ticksUntilRestart = restartInterval;
                } else if (ticksUntilRestart == WARNING_TIME) {
                    Bukkit.broadcastMessage(plugin.getConfigManager().getMessage("restart-warning")
                        .replace("%minutes%", "1"));
                }
                ticksUntilRestart--;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void restartMobAreas() {
        // Broadcast restart message
        Bukkit.broadcastMessage(plugin.getConfigManager().getMessage("restart-complete"));

        // Restart each spawn area
        plugin.getAreaManager().getAllAreas().values().forEach(area -> {
            plugin.getSpawnManager().restartArea(area);
        });
    }

    public void stop() {
        if (restartTask != null) {
            restartTask.cancel();
        }
    }
}