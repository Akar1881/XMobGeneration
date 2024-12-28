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

        try {
            EntityType mobType = EntityType.valueOf(args[2].toUpperCase());
            int spawnCount = Integer.parseInt(args[3]);
            int respawnDelay = Integer.parseInt(args[4]);

            if (spawnCount < 1 || respawnDelay < 1) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
                return true;
            }

            area.setMobType(mobType);
            area.setSpawnCount(spawnCount);
            area.setRespawnDelay(respawnDelay);

            plugin.getAreaManager().saveAreas();
            plugin.getSpawnManager().startSpawning(area);

            player.sendMessage(plugin.getConfigManager().getMessage("area-configured")
                .replace("%name%", areaName));

        } catch (IllegalArgumentException e) {
            player.sendMessage(plugin.getConfigManager().getMessage("invalid-mob-type"));
            return true;
        }

        return true;
    }
}