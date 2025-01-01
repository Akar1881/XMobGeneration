package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Random;

public class LocationFinder {
    private final Random random = new Random();

    public Location findSafeSpawnLocation(SpawnArea area) {
        Location pos1 = area.getPos1();
        Location pos2 = area.getPos2();
        World world = pos1.getWorld();
        
        if (world == null) return null;

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        for (int attempt = 0; attempt < 10; attempt++) {
            int x = random.nextInt(maxX - minX + 1) + minX;
            int z = random.nextInt(maxZ - minZ + 1) + minZ;
            
            for (int y = minY; y <= maxY; y++) {
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