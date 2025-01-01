package com.xmobgeneration.commands.subcommands;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GetWandCommand implements SubCommand {
    private final XMobGeneration plugin;

    public GetWandCommand(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission("xmg.boss")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        ItemStack wand = new ItemStack(Material.GOLDEN_SHOVEL);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName("§6Boss Spawn Wand");
        meta.setLore(Arrays.asList(
            "§7Break a block to set boss spawn point",
            "§7Must be used inside an existing area"
        ));
        wand.setItemMeta(meta);
        
        player.getInventory().addItem(wand);
        player.sendMessage("§aYou have received the Boss Spawn Wand!");
        player.sendMessage("§7Break a block inside an area to set the boss spawn point.");
        
        return true;
    }
}