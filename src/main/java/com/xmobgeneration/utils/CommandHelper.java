package com.xmobgeneration.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper {
    public static List<CommandInfo> getCommands() {
        List<CommandInfo> commands = new ArrayList<>();
        
        // General Commands
        commands.add(new CommandInfo("create", "<n>", "Create a new spawn area"));
        commands.add(new CommandInfo("delete", "<n>", "Delete a spawn area"));
        commands.add(new CommandInfo("list", "", "List all spawn areas"));
        commands.add(new CommandInfo("gui", "", "Open the GUI interface"));
        commands.add(new CommandInfo("reload", "", "Reload config and restart all mob areas"));
        commands.add(new CommandInfo("xp", "<area> <amount>", "Set XP amount for an area"));
        
        // Mob Configuration Commands
        commands.add(new CommandInfo("config", "<n> <mobType|mythic:mobType> <count> <delay>", "Configure an area"));
        commands.add(new CommandInfo("setmobnames", "<areaname> <mobname>", "Set custom name for mobs in an area"));
        commands.add(new CommandInfo("mobconfig", "<areaname> <health> <damage> <level>", "Configure mob stats"));
        commands.add(new CommandInfo("levelrange", "<area> <minLevel> <maxLevel>", "Set level range for mobs in an area"));
        commands.add(new CommandInfo("proximity", "<area> <true|false> <range>", "Set player proximity requirement for spawning"));
        
        // Boss Commands
        commands.add(new CommandInfo("getwand", "", "Get the boss spawn point wand"));
        commands.add(new CommandInfo("configboss", "<n> <mobType|mythic:mobType> <respawnDelay>", "Configure a boss area"));
        commands.add(new CommandInfo("bosslist", "", "List all boss areas"));
        
        return commands;
    }
    
    public static class CommandInfo {
        private final String command;
        private final String args;
        private final String description;
        
        public CommandInfo(String command, String args, String description) {
            this.command = command;
            this.args = args;
            this.description = description;
        }
        
        public String getCommand() { return command; }
        public String getArgs() { return args; }
        public String getDescription() { return description; }
    }
}