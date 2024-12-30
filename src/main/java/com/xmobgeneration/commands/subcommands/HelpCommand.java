package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {
    private final XMobGeneration plugin;

    public HelpCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage("§6=== XMobGeneration Help ===");
        player.sendMessage("§e/xmg create <name> §7- Create a new spawn area");
        player.sendMessage("§e/xmg delete <name> §7- Delete a spawn area");
        player.sendMessage("§e/xmg config <name> <mobType|mythic:mobType> <count> <delay> §7- Configure an area");
        player.sendMessage("§e/xmg setmobnames <areaname> <mobsname> §7- Set Mobs Name in an area");
        player.sendMessage("§e/xmg mobconfig <areaname> <mobHealth> <mobDamage> <mobLevel> §7- Configure Mobs in an area");
        player.sendMessage("§e/xmg list §7- List all spawn areas");
        player.sendMessage("§e/xmg gui §7- Open the GUI");
        player.sendMessage("§e/xmg reload §7- Reload config and restart all mob areas");
        player.sendMessage("§e/xmg help §7- Show this help message");
        return true;
    }
}