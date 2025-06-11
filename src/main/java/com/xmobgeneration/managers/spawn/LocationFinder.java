package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Random;

public class LocationFinder {
    private final Random random = new Random();

    public Location findSafeSpawnLocation(SpawnArea area) {
        Location pos1 = area.getPos1();
        Location pos2 = area.getPos2();
        World world = pos1.getWorld();
        
        if (world == null) return null;

        // Check if player proximity is required and if there are players nearby
        if (area.isPlayerProximityRequired()) {
            // Calculate area center for distance checks
            int centerX = (Math.min(pos1.getBlockX(), pos2.getBlockX()) + Math.max(pos1.getBlockX(), pos2.getBlockX())) / 2;
            int centerZ = (Math.min(pos1.getBlockZ(), pos2.getBlockZ()) + Math.max(pos1.getBlockZ(), pos2.getBlockZ())) / 2;
            int centerY = (Math.min(pos1.getBlockY(), pos2.getBlockY()) + Math.max(pos1.getBlockY(), pos2.getBlockY())) / 2;
            Location centerLoc = new Location(world, centerX, centerY, centerZ);
            
            // Get nearby players using a more efficient method
            boolean playersNearby = false;
            int range = area.getProximityRange();
            for (Player player : world.getPlayers()) {
                if (player.getLocation().distance(centerLoc) <= range) {
                    playersNearby = true;
                    break;
                }
            }
            
            if (!playersNearby) {
                return null; // No players within range
            }
        }

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        // Calculate area size and scale attempts accordingly
        int areaWidth = maxX - minX + 1;
        int areaLength = maxZ - minZ + 1;
        int areaSize = areaWidth * areaLength;
        int maxAttempts = Math.min(100, Math.max(10, areaSize / 100)); // Scale attempts based on area size

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = random.nextInt(maxX - minX + 1) + minX;
            int z = random.nextInt(maxZ - minZ + 1) + minZ;
            
            // Try from top to bottom for better spawn chances
            for (int y = maxY; y >= minY; y--) {
                Location testLoc = new Location(world, x, y, z);
                if (isValidSpawnLocation(testLoc)) {
                    // Add 1.0 to Y coordinate to ensure entity spawns above the block
                    return testLoc.add(0.5, 1.0, 0.5);
                }
            }
        }
        
        return null;
    }

    private boolean isValidSpawnLocation(Location location) {
        Block block = location.getBlock();
        Block below = location.clone().subtract(0, 1, 0).getBlock();
        Block above = location.clone().add(0, 1, 0).getBlock();
        Block twoAbove = location.clone().add(0, 2, 0).getBlock();
        
        return block.isEmpty() && 
               above.isEmpty() && 
               twoAbove.isEmpty() && 
               below.getType().isSolid();
    }
}