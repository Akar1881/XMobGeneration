package com.xmobgeneration.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {
    public static String serialize(Location location) {
        if (location == null) return "";
        if (location.getWorld() == null) return "";
        return String.format("%s,%d,%d,%d,%f,%f",
            location.getWorld().getName(),
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ(),
            location.getYaw(),
            location.getPitch()
        );
    }

    public static Location deserialize(String str) {
        if (str == null || str.isEmpty()) return null;
        String[] parts = str.split(",");
        if (parts.length != 6) return null;
        
        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;
        
        try {
            return new Location(
                world,
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}