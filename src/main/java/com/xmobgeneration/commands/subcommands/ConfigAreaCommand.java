package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import com.xmobgeneration.commands.SubCommand;

public class ConfigAreaCommand implements SubCommand {
    private final XMobGeneration plugin;

    public ConfigAreaCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String[] args) {
        if (args.length < 5) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-configarea"));
            return true;
        }

        String areaName = args[1];
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("area-not-found")
                .replace("%name%", areaName));
            return true;
        }

        String mobTypeStr = args[2];
        int spawnCount;
        int respawnDelay;

        try {
            spawnCount = Integer.parseInt(args[3]);
            respawnDelay = Integer.parseInt(args[4]);

            if (spawnCount < 1 || respawnDelay < 1) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
            return true;
        }

        // Check if it's a MythicMob
        if (mobTypeStr.toLowerCase().startsWith("mythic:")) {
            String mythicMobType = mobTypeStr.substring(7); // Remove "mythic:" prefix
            
            if (!plugin.getMythicMobsManager().isMythicMobsEnabled()) {
                player.sendMessage(plugin.getConfigManager().getMessage("mythicmobs-not-installed"));
                return true;
            }
            
            if (!plugin.getMythicMobsManager().isMythicMob(mythicMobType)) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-mythicmob-type"));
                return true;
            }
            
            area.setMythicMob(true);
            area.setMythicMobType(mythicMobType);
        } else {
            // Handle vanilla mob type
            try {
                EntityType mobType = EntityType.valueOf(mobTypeStr.toUpperCase());
                area.setMythicMob(false);
                area.setMobType(mobType);
            } catch (IllegalArgumentException e) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-mob-type"));
                return true;
            }
        }

        area.setSpawnCount(spawnCount);
        area.setRespawnDelay(respawnDelay);

        plugin.getAreaManager().saveAreas();
        plugin.getSpawnManager().startSpawning(area);

        player.sendMessage(plugin.getConfigManager().getMessage("area-configured")
            .replace("%name%", areaName));

        return true;
    }
}