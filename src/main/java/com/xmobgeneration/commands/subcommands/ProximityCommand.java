package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProximityCommand implements SubCommand {
    private final XMobGeneration plugin;

    public ProximityCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-proximity"));
            return true;
        }

        String areaName = args[1];
        SpawnArea area = plugin.getAreaManager().getArea(areaName);

        if (area == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("area-not-found").replace("%name%", areaName));
            return true;
        }

        try {
            boolean enabled = Boolean.parseBoolean(args[2].toLowerCase());
            int range = Integer.parseInt(args[3]);

            if (range < 10) {
                range = 10; // Minimum range
            }

            area.setPlayerProximityRequired(enabled);
            area.setProximityRange(range);
            plugin.getAreaManager().saveAreas();

            player.sendMessage(plugin.getConfigManager().getMessage("proximity-set")
                    .replace("%name%", areaName)
                    .replace("%enabled%", enabled ? "enabled" : "disabled")
                    .replace("%range%", String.valueOf(range)));

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
        } else if (args.length == 3) {
            Arrays.asList("true", "false").stream()
                    .filter(option -> option.startsWith(args[2].toLowerCase()))
                    .forEach(completions::add);
        }

        return completions;
    }
}