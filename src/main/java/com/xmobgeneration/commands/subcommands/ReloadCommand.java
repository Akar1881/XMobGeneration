package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand {
    private final XMobGeneration plugin;

    public ReloadCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        // Reload configuration
        plugin.getConfigManager().reloadConfig();
        plugin.getAreaManager().loadAreas();

        // Restart all mob areas
        plugin.getAreaManager().getAllAreas().values().forEach(area -> {
            plugin.getSpawnManager().restartArea(area);
        });

        player.sendMessage("§aConfiguration reloaded and all mob areas restarted!");
        return true;
    }
}