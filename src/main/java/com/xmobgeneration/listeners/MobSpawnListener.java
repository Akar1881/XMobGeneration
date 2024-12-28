package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnListener implements Listener {
    private final XMobGeneration plugin;

    public MobSpawnListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            Location loc = event.getLocation();
            
            // Check if spawn location is within any spawn area
            for (SpawnArea area : plugin.getAreaManager().getAllAreas().values()) {
                if (isLocationInArea(loc, area)) {
                    // Cancel natural spawns in spawn areas
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private boolean isLocationInArea(Location loc, SpawnArea area) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        Location pos1 = area.getPos1();
        Location pos2 = area.getPos2();

        return x >= Math.min(pos1.getBlockX(), pos2.getBlockX()) &&
               x <= Math.max(pos1.getBlockX(), pos2.getBlockX()) &&
               y >= Math.min(pos1.getBlockY(), pos2.getBlockY()) &&
               y <= Math.max(pos1.getBlockY(), pos2.getBlockY()) &&
               z >= Math.min(pos1.getBlockZ(), pos2.getBlockZ()) &&
               z <= Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    }
}