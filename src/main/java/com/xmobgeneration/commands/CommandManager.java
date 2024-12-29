package com.xmobgeneration.commands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final XMobGeneration plugin;
    private final Map<String, SubCommand> commands = new HashMap<>();

    public CommandManager(XMobGeneration plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    private void registerCommands() {
        commands.put("create", new CreateAreaCommand(plugin));
        commands.put("delete", new DeleteAreaCommand(plugin));
        commands.put("config", new ConfigAreaCommand(plugin));
        commands.put("list", new ListAreasCommand(plugin));
        commands.put("help", new HelpCommand(plugin));
        commands.put("gui", new GuiCommand(plugin));
        commands.put("setmobnames", new SetMobNamesCommand(plugin));
        commands.put("mobconfig", new MobConfigCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("xmg.admin")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            commands.get("help").execute(player, args);
            return true;
        }

        SubCommand subCommand = commands.get(args[0].toLowerCase());
        if (subCommand == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("unknown-command"));
            return true;
        }

        return subCommand.execute(player, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .forEach(completions::add);
        } else if (args.length == 2) {
            SubCommand subCommand = commands.get(args[0].toLowerCase());
            if (subCommand != null) {
                completions.addAll(subCommand.getTabCompletions(sender, args));
            }
        }

        return completions;
    }
}