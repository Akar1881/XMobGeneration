package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import com.xmobgeneration.utils.CommandHelper;
import com.xmobgeneration.utils.CommandHelper.CommandInfo;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {
    private final XMobGeneration plugin;

    public HelpCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage("§6=== XMobGeneration Help ===");
        
        // General Commands Section
        player.sendMessage("§e§lGeneral Commands:");
        for (CommandInfo cmd : CommandHelper.getCommands()) {
            if (!cmd.getCommand().contains("boss") && !cmd.getCommand().contains("mob")) {
                sendCommandHelp(player, cmd);
            }
        }
        
        // Mob Configuration Section
        player.sendMessage("\n§e§lMob Configuration:");
        for (CommandInfo cmd : CommandHelper.getCommands()) {
            if (cmd.getCommand().contains("mob") || cmd.getCommand().equals("config")) {
                sendCommandHelp(player, cmd);
            }
        }
        
        // Boss Commands Section
        player.sendMessage("\n§e§lBoss Commands:");
        for (CommandInfo cmd : CommandHelper.getCommands()) {
            if (cmd.getCommand().contains("boss") || cmd.getCommand().equals("getwand")) {
                sendCommandHelp(player, cmd);
            }
        }
        
        return true;
    }
    
    private void sendCommandHelp(Player player, CommandInfo cmd) {
        String command = "/xmg " + cmd.getCommand();
        String args = cmd.getArgs().isEmpty() ? "" : " " + cmd.getArgs();
        player.sendMessage("§e" + command + "§7" + args + " §7- " + cmd.getDescription());
    }
}