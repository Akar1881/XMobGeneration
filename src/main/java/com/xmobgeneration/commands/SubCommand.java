package com.xmobgeneration.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface SubCommand {
    boolean execute(Player player, String[] args);
    
    default List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}