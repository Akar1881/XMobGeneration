package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;

public class SetXPCommand implements SubCommand {
    private final XMobGeneration plugin;

    public SetXPCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-setxp"));
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
            int xpAmount = Integer.parseInt(args[2]);
            if (xpAmount < 0) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-xp-amount"));
                return true;
            }

            area.setXpAmount(xpAmount);
            plugin.getAreaManager().saveAreas();
            
            player.sendMessage(plugin.getConfigManager().getMessage("xp-set")
                .replace("%name%", areaName)
                .replace("%amount%", String.valueOf(xpAmount)));
                
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
            return true;
        }

        return true;
    }
}