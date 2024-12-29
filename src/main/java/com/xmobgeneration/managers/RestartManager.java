package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RestartManager {
    private final XMobGeneration plugin;
    private BukkitTask restartTask;
    private static final long RESTART_INTERVAL = 1275L; // 10 minutes in ticks (20 ticks = 1 second)
    private static final long WARNING_TIME = 1200L; // 1 minute in ticks

    public RestartManager(XMobGeneration plugin) {
        this.plugin = plugin;
        startRestartTask();
    }

    private void startRestartTask() {
        restartTask = new BukkitRunnable() {
            private long ticksUntilRestart = RESTART_INTERVAL;

            @Override
            public void run() {
                if (ticksUntilRestart <= 0) {
                    restartMobAreas();
                    ticksUntilRestart = RESTART_INTERVAL;
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