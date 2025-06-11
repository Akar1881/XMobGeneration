package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LevelRangeCommand implements SubCommand {
    private final XMobGeneration plugin;

    public LevelRangeCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-levelrange"));
            return true;
        }

        String areaName = args[1];
        SpawnArea area = plugin.getAreaManager().getArea(areaName);

        if (area == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("area-not-found").replace("%name%", areaName));
            return true;
        }

        try {
            int minLevel = Integer.parseInt(args[2]);
            int maxLevel = Integer.parseInt(args[3]);

            if (minLevel < 1) {
                player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
                return true;
            }

            if (maxLevel < minLevel) {
                maxLevel = minLevel;
            }

            area.setMinLevel(minLevel);
            area.setMaxLevel(maxLevel);
            plugin.getAreaManager().saveAreas();

            player.sendMessage(plugin.getConfigManager().getMessage("level-range-set")
                    .replace("%name%", areaName)
                    .replace("%min%", String.valueOf(minLevel))
                    .replace("%max%", String.valueOf(maxLevel)));

        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            plugin.getAreaManager().getAllAreas().keySet().stream()
                    .filter(name -> name.startsWith(args[1]))
                    .forEach(completions::add);
        }

        return completions;
    }
}