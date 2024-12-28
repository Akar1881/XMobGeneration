package com.xmobgeneration.listeners;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.xmobgeneration.XMobGeneration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AreaSelectionListener implements Listener {
    private final XMobGeneration plugin;
    private static final Material WAND_MATERIAL = Material.WOODEN_AXE;

    public AreaSelectionListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null) return;
        if (!player.hasPermission("xmg.admin")) return;
        
        WorldEditPlugin worldEdit = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit == null) return;

        // Check if player is using WorldEdit wand
        if (item.getType() == WAND_MATERIAL) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                player.sendMessage(plugin.getConfigManager().getMessage("pos1-set"));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                player.sendMessage(plugin.getConfigManager().getMessage("pos2-set"));
            }
        }
    }
}