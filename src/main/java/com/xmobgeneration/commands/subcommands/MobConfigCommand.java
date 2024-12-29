package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;

public class MobConfigCommand implements SubCommand {
    private final XMobGeneration plugin;

    public MobConfigCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 5) {
            player.sendMessage(plugin.getConfigManager().getMessage("usage-mobconfig"));
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
            double health = Double.parseDouble(args[2]);
            double damage = Double.parseDouble(args[3]);
            int level = Integer.parseInt(args[4]);

            area.getMobStats().setHealth(health);
            area.getMobStats().setDamage(damage);
            area.getMobStats().setLevel(level);
            
            plugin.getAreaManager().saveAreas();
            
            player.sendMessage(plugin.getConfigManager().getMessage("mobconfig-set")
                .replace("%name%", areaName)
                .replace("%health%", String.valueOf(health))
                .replace("%damage%", String.valueOf(damage))
                .replace("%level%", String.valueOf(level)));

            // Restart the area to apply changes to existing mobs
            plugin.getSpawnManager().restartArea(area);

        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getConfigManager().getMessage("invalid-numbers"));
            return true;
        }

        return true;
    }
}