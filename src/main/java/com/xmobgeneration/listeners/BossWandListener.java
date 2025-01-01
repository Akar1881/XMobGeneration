package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BossWandListener implements Listener {
    private final XMobGeneration plugin;

    public BossWandListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (!isWand(item)) return;
        
        event.setCancelled(true);
        Location location = event.getBlock().getLocation();
        
        // Find which area contains this location
        SpawnArea targetArea = null;
        for (SpawnArea area : plugin.getAreaManager().getAllAreas().values()) {
            if (isLocationInArea(location, area)) {
                targetArea = area;
                break;
            }
        }
        
        if (targetArea == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("pos-not-in-area"));
            return;
        }
        
        targetArea.setBossArea(true);
        targetArea.setBossSpawnPoint(location);
        plugin.getAreaManager().saveAreas();
        
        player.sendMessage(plugin.getConfigManager().getMessage("boss-pos-set")
            .replace("%name%", targetArea.getName()));
    }

    private boolean isWand(ItemStack item) {
        if (item == null || item.getType() != Material.GOLDEN_SHOVEL) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName() && 
               meta.getDisplayName().equals("§6Boss Spawn Wand");
    }

    private boolean isLocationInArea(Location loc, SpawnArea area) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        Location pos1 = area.getPos1();
        Location pos2 = area.getPos2();

        return x >= Math.min(pos1.getBlockX(), pos2.getBlockX()) &&
               x <= Math.max(pos1.getBlockX(), pos2.getBlockX()) &&
               y >= Math.min(pos1.getBlockY(), pos2.getBlockY()) &&
               y <= Math.max(pos1.getBlockY(), pos2.getBlockY()) &&
               z >= Math.min(pos1.getBlockZ(), pos2.getBlockZ()) &&
               z <= Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    }
}