package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import org.bukkit.entity.Player;

public class GuiCommand implements SubCommand {
    private final XMobGeneration plugin;

    public GuiCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        plugin.getGUIManager().openMainGUI(player);
        return true;
    }
}