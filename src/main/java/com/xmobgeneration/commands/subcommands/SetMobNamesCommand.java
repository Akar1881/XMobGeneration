package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;

public class SetMobNamesCommand implements SubCommand {
    private final XMobGeneration plugin;

    public SetMobNamesCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-setmobnames"));
            return true;
        }

        String areaName = args[1];
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("area-not-found")
                .replace("%name%", areaName));
            return true;
        }

        // Combine remaining args for the mob name
        StringBuilder mobName = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            if (i > 2) mobName.append(" ");
            mobName.append(args[i]);
        }

        area.getMobStats().setMobName(mobName.toString());
        area.getMobStats().setShowName(true);
        plugin.getAreaManager().saveAreas();

        player.sendMessage(plugin.getConfigManager().getMessage("mobname-set")
            .replace("%name%", areaName)
            .replace("%mobname%", mobName.toString()));

        // Restart the area to apply changes to existing mobs
        plugin.getSpawnManager().restartArea(area);

        return true;
    }
}