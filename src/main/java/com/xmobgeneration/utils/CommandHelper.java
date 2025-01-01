package com.xmobgeneration.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper {
    public static List<CommandInfo> getCommands() {
        List<CommandInfo> commands = new ArrayList<>();
        
        // General Commands
        commands.add(new CommandInfo("create", "<name>", "Create a new spawn area"));
        commands.add(new CommandInfo("delete", "<name>", "Delete a spawn area"));
        commands.add(new CommandInfo("list", "", "List all spawn areas"));
        commands.add(new CommandInfo("gui", "", "Open the GUI interface"));
        commands.add(new CommandInfo("reload", "", "Reload config and restart all mob areas"));
        
        // Mob Configuration Commands
        commands.add(new CommandInfo("config", "<name> <mobType|mythic:mobType> <count> <delay>", "Configure an area"));
        commands.add(new CommandInfo("setmobnames", "<areaname> <mobname>", "Set custom name for mobs in an area"));
        commands.add(new CommandInfo("mobconfig", "<areaname> <health> <damage> <level>", "Configure mob stats"));
        
        // Boss Commands
        commands.add(new CommandInfo("getwand", "", "Get the boss spawn point wand"));
        commands.add(new CommandInfo("configboss", "<name> <mobType|mythic:mobType> <respawnDelay>", "Configure a boss area"));
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