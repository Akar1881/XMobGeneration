package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ConfigBossCommand implements SubCommand {
    private final XMobGeneration plugin;

    public ConfigBossCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission("xmg.boss")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-configboss"));
            return true;
        }

        String areaName = args[1];
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("area-not-found")
                .replace("%name%", areaName));
            return true;
        }

        if (!area.isBossArea()) {
            player.sendMessage(plugin.getConfigManager().getMessage("not-boss-area"));
            return true;
        }

        if (area.getBossSpawnPoint() == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-boss-spawn"));
            return true;
        }

        String mobTypeStr = args[2];
        int respawnDelay;

        try {
            respawnDelay = Integer.parseInt(args[3]);
            if (respawnDelay < 1) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
            return true;
        }

        // Check if it's a MythicMob
        if (mobTypeStr.toLowerCase().startsWith("mythic:")) {
            String mythicMobType = mobTypeStr.substring(7);
            
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
            try {
                EntityType mobType = EntityType.valueOf(mobTypeStr.toUpperCase());
                area.setMythicMob(false);
                area.setMobType(mobType);
            } catch (IllegalArgumentException e) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-mob-type"));
                return true;
            }
        }

        area.setSpawnCount(1); // Boss areas always have 1 mob
        area.setRespawnDelay(respawnDelay);

        plugin.getAreaManager().saveAreas();
        plugin.getSpawnManager().startSpawning(area);

        player.sendMessage(plugin.getConfigManager().getMessage("boss-configured")
            .replace("%name%", areaName));

        return true;
    }
}