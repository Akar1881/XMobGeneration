package com.xmobgeneration.commands.subcommands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import com.xmobgeneration.XMobGeneration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.xmobgeneration.commands.SubCommand;

public class CreateAreaCommand implements SubCommand {
    private final XMobGeneration plugin;

    public CreateAreaCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-createarea"));
            return true;
        }

        if (plugin.getAreaManager().hasReachedMaxAreas()) {
            player.sendMessage(plugin.getConfigManager().getMessage("max-areas-reached"));
            return true;
        }

        String areaName = args[1];
        if (plugin.getAreaManager().getArea(areaName) != null) {
            player.sendMessage(plugin.getConfigManager().getMessage("area-exists"));
            return true;
        }

        WorldEditPlugin worldEdit = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
        try {
            Region region = worldEdit.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
            if (region == null) {
                player.sendMessage(plugin.getConfigManager().getMessage("no-selection"));
                return true;
            }

            Location pos1 = BukkitAdapter.adapt(player.getWorld(), region.getMinimumPoint());
            Location pos2 = BukkitAdapter.adapt(player.getWorld(), region.getMaximumPoint());
            
            plugin.getAreaManager().createArea(areaName, pos1, pos2);
            player.sendMessage(plugin.getConfigManager().getMessage("area-created")
                .replace("%name%", areaName));
            
        } catch (Exception e) {
            player.sendMessage(plugin.getConfigManager().getMessage("selection-error"));
            return true;
        }

        return true;
    }
}