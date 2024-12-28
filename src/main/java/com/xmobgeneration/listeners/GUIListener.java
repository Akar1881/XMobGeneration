package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    private final XMobGeneration plugin;

    public GUIListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        String title = event.getView().getTitle();
        if (!title.startsWith("§8")) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null) return;

        if (title.equals("§8XMobGeneration")) {
            handleMainMenu(player, clicked);
        } else if (title.startsWith("§8Areas - ")) {
            handleAreaList(player, clicked, title);
        } else if (title.startsWith("§8Edit Area - ")) {
            handleAreaEdit(player, clicked, title);
        }
    }

    private void handleMainMenu(Player player, ItemStack clicked) {
        switch (clicked.getType()) {
            case EMERALD_BLOCK:
                player.closeInventory();
                player.sendMessage("§aUse /xmg create <name> to create a new area");
                break;
            case ANVIL:
                plugin.getGUIManager().openAreaListGUI(player, "edit");
                break;
            case REDSTONE_BLOCK:
                plugin.getGUIManager().openAreaListGUI(player, "delete");
                break;
        }
    }

    private void handleAreaList(Player player, ItemStack clicked, String title) {
        if (!clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName()) return;
        
        String areaName = clicked.getItemMeta().getDisplayName().substring(2); // Remove §e
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) return;

        if (title.endsWith("edit")) {
            plugin.getGUIManager().openAreaEditGUI(player, area);
        } else if (title.endsWith("delete")) {
            plugin.getAreaManager().deleteArea(areaName);
            player.sendMessage(plugin.getConfigManager().getMessage("area-deleted")
                .replace("%name%", areaName));
            plugin.getGUIManager().openAreaListGUI(player, "delete");
        } else if (title.endsWith("list")) {
            boolean enabled = plugin.getSpawnManager().toggleSpawning(area);
            player.sendMessage("§7Spawning for area §e" + areaName + " §7is now " + 
                (enabled ? "§aenabled" : "§cdisabled"));
            plugin.getAreaManager().saveAreas();
            plugin.getGUIManager().openAreaListGUI(player, "list");
        }
    }

    private void handleAreaEdit(Player player, ItemStack clicked, String title) {
        String areaName = title.substring("§8Edit Area - ".length());
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) return;

        switch (clicked.getType()) {
            case LIME_DYE:
            case GRAY_DYE:
                boolean enabled = plugin.getSpawnManager().toggleSpawning(area);
                player.sendMessage("§7Spawning for area §e" + areaName + " §7is now " + 
                    (enabled ? "§aenabled" : "§cdisabled"));
                plugin.getAreaManager().saveAreas();
                plugin.getGUIManager().openAreaEditGUI(player, area);
                break;
        }
    }
}