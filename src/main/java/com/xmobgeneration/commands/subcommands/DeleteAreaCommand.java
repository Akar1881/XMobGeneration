package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.entity.Player;
import com.xmobgeneration.commands.SubCommand;

public class DeleteAreaCommand implements SubCommand {
    private final XMobGeneration plugin;

    public DeleteAreaCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-deletearea"));
            return true;
        }

        String areaName = args[1];
        if (plugin.getAreaManager().getArea(areaName) == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("area-not-found")
                .replace("%name%", areaName));
            return true;
        }

        plugin.getAreaManager().deleteArea(areaName);
        player.sendMessage(plugin.getConfigManager().getMessage("area-deleted")
            .replace("%name%", areaName));

        return true;
    }
}