package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;

import java.util.Map;

public class BossListCommand implements SubCommand {
    private final XMobGeneration plugin;

    public BossListCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission("xmg.boss")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        Map<String, SpawnArea> areas = plugin.getAreaManager().getAllAreas();
        boolean foundBossAreas = false;
        
        player.sendMessage("§6=== Boss Areas ===");
        
        for (SpawnArea area : areas.values()) {
            if (area.isBossArea()) {
                foundBossAreas = true;
                String mobType = area.isMythicMob() ? 
                    "mythic:" + area.getMythicMobType() : 
                    area.getMobType().toString();
                
                player.sendMessage(String.format("§e%s §7- Type: §f%s§7, Delay: §f%ds",
                    area.getName(),
                    mobType,
                    area.getRespawnDelay()
                ));
            }
        }
        
        if (!foundBossAreas) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-boss-areas"));
        }
        
        return true;
    }
}