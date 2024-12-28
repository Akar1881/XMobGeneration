package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;

import java.util.Map;

public class ListAreasCommand implements SubCommand {
    private final XMobGeneration plugin;

    public ListAreasCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        Map<String, SpawnArea> areas = plugin.getAreaManager().getAllAreas();
        
        if (areas.isEmpty()) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-areas"));
            return true;
        }

        player.sendMessage("§6=== Spawn Areas ===");
        for (SpawnArea area : areas.values()) {
            player.sendMessage(String.format("§e%s §7- Type: §f%s§7, Count: §f%d§7, Delay: §f%ds",
                area.getName(),
                area.getMobType(),
                area.getSpawnCount(),
                area.getRespawnDelay()
            ));
        }
        return true;
    }
}