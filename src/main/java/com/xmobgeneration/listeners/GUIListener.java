package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class GUIListener implements Listener {
    private final XMobGeneration plugin;

    public GUIListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();
        if (!title.startsWith("§8")) return;

        if (title.startsWith("§8Custom Drops - ")) {
            // Cancel if any dragged slot is in the bottom row
            for (int slot : event.getRawSlots()) {
                if (slot >= 45) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else {
            // Cancel drag for all other GUIs
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        String title = event.getView().getTitle();
        if (!title.startsWith("§8")) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null && !title.startsWith("§8Custom Drops - ")) return;

        if (title.equals("§8XMobGeneration")) {
            event.setCancelled(true);
            handleMainMenu(player, clicked);
        } else if (title.startsWith("§8Areas - ")) {
            event.setCancelled(true);
            handleAreaList(player, clicked, title);
        } else if (title.startsWith("§8Edit Area - ")) {
            event.setCancelled(true);
            handleAreaEdit(player, clicked, title);
        } else if (title.startsWith("§8Custom Drops - ")) {
            handleCustomDrops(event, title);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (title.startsWith("§8Custom Drops - ")) {
            String areaName = title.substring("§8Custom Drops - ".length());
            SpawnArea area = plugin.getAreaManager().getArea(areaName);
            if (area != null) {
                List<ItemStack> items = new ArrayList<>();
                for (int i = 0; i < 45; i++) {
                    ItemStack item = event.getInventory().getItem(i);
                    if (item != null) {
                        items.add(item.clone());
                    }
                }
                area.getCustomDrops().setItems(items);
                plugin.getAreaManager().saveAreas();
            }
        }
    }

    private void handleCustomDrops(InventoryClickEvent event, String title) {
        String areaName = title.substring("§8Custom Drops - ".length());
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) return;

        int slot = event.getRawSlot();
        
        // Allow modifications in the first 45 slots and player inventory
        if (slot >= 45 && slot < event.getInventory().getSize()) {
            event.setCancelled(true);
            
            // Handle toggle button click
            if (slot == 49 && event.getCurrentItem() != null) {
                area.getCustomDrops().setEnabled(!area.getCustomDrops().isEnabled());
                plugin.getAreaManager().saveAreas();
                plugin.getGUIManager().openCustomDropsMenu((Player) event.getWhoClicked(), area);
            }
            // Handle save button click
            else if (slot == 53) {
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage("§aCustom drops saved successfully!");
            }
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
            case CHEST:
                plugin.getGUIManager().openCustomDropsMenu(player, area);
                break;
        }
    }
}