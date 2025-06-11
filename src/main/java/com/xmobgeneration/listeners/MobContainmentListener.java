package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MobContainmentListener implements Listener {
    private final XMobGeneration plugin;
    private static final long CHECK_INTERVAL = 100L; // Check every 5 seconds (100 ticks)

    public MobContainmentListener(XMobGeneration plugin) {
        this.plugin = plugin;
        startContainmentChecker();
    }

    private void startContainmentChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Only check mobs in active areas
                for (SpawnArea area : plugin.getAreaManager().getAllAreas().values()) {
                    if (!area.isEnabled()) continue;
                    
                    plugin.getSpawnManager().getMobTracker().getAllMobs().stream()
                        .filter(spawnedMob -> spawnedMob.getAreaName().equals(area.getName()))
                        .forEach(spawnedMob -> {
                            Entity entity = spawnedMob.getEntity();
                            if (entity != null && !entity.isDead()) {
                                if (!isLocationInArea(entity.getLocation(), area)) {
                                    Location safeLocation = findSafeLocation(entity.getLocation(), area);
                                    if (safeLocation != null) {
                                        entity.teleport(safeLocation);
                                    }
                                }
                            }
                        });
                }
            }
        }.runTaskTimer(plugin, CHECK_INTERVAL, CHECK_INTERVAL);
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        SpawnedMob spawnedMob = plugin.getSpawnManager().getMobTracker().getMob(entity.getUniqueId());
        
        if (spawnedMob != null) {
            SpawnArea area = plugin.getAreaManager().getArea(spawnedMob.getAreaName());
            if (area != null && !isLocationInArea(event.getTo(), area)) {
                event.setCancelled(true);
                Location safeLocation = findSafeLocation(entity.getLocation(), area);
                if (safeLocation != null) {
                    event.setTo(safeLocation);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        SpawnedMob spawnedMob = plugin.getSpawnManager().getMobTracker().getMob(entity.getUniqueId());
        
        if (spawnedMob != null) {
            // Prevent players from using leads, boats, etc. to move mobs
            event.setCancelled(true);
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

    private Location findSafeLocation(Location current, SpawnArea area) {
        Location pos1 = area.getPos1();
        Location pos2 = area.getPos2();
        
        int x = current.getBlockX();
        int y = current.getBlockY();
        int z = current.getBlockZ();
        
        // Clamp coordinates to area bounds
        x = Math.min(Math.max(x, Math.min(pos1.getBlockX(), pos2.getBlockX())), 
            Math.max(pos1.getBlockX(), pos2.getBlockX()));
        y = Math.min(Math.max(y, Math.min(pos1.getBlockY(), pos2.getBlockY())), 
            Math.max(pos1.getBlockY(), pos2.getBlockY()));
        z = Math.min(Math.max(z, Math.min(pos1.getBlockZ(), pos2.getBlockZ())), 
            Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
        
        return new Location(current.getWorld(), x + 0.5, y, z + 0.5, 
            current.getYaw(), current.getPitch());
    }
}