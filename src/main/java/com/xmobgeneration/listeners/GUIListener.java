package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;

import org.bukkit.Material;
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
        } else if (title.startsWith("§8Mob Equipment - ")) {
            handleMobEquipment(event);
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
                List<Double> chances = new ArrayList<>();
                
                // Collect items and set default chances
                for (int i = 0; i < 45; i++) {
                    ItemStack item = event.getInventory().getItem(i);
                    if (item != null) {
                        items.add(item.clone());
                        chances.add(100.0); // Default 100% chance
                    }
                }
                
                area.getCustomDrops().setItems(items, chances);
                plugin.getAreaManager().saveAreas();
            }
        }
    }

    private void handleMobEquipment(InventoryClickEvent event) {
        String areaName = event.getView().getTitle().substring("§8Mob Equipment - ".length());
        SpawnArea area = plugin.getAreaManager().getArea(areaName);
        
        if (area == null) return;

        int slot = event.getRawSlot();
        ItemStack clicked = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        if (slot >= 0 && slot <= 4) {
            event.setCancelled(true);

            if (cursor != null && cursor.getType() != Material.AIR) {
                boolean validItem = false;
                switch (slot) {
                    case 0: // Helmet
                        validItem = isHelmet(cursor.getType());
                        if (validItem) area.getMobEquipment().setHelmet(cursor.clone());
                        break;
                    case 1: // Chestplate
                        validItem = isChestplate(cursor.getType());
                        if (validItem) area.getMobEquipment().setChestplate(cursor.clone());
                        break;
                    case 2: // Leggings
                        validItem = isLeggings(cursor.getType());
                        if (validItem) area.getMobEquipment().setLeggings(cursor.clone());
                        break;
                    case 3: // Boots
                        validItem = isBoots(cursor.getType());
                        if (validItem) area.getMobEquipment().setBoots(cursor.clone());
                        break;
                    case 4: // Off-hand item
                        area.getMobEquipment().setOffHand(cursor.clone());
                        validItem = true;
                        break;
                }

                if (validItem) {
                    plugin.getAreaManager().saveAreas();
                    plugin.getGUIManager().openCustomMobEquipmentMenu((Player) event.getWhoClicked(), area);
                }
            } else if (clicked != null && clicked.getType() != Material.ARMOR_STAND) {
                // Remove equipment
                switch (slot) {
                    case 0: area.getMobEquipment().setHelmet(null); break;
                    case 1: area.getMobEquipment().setChestplate(null); break;
                    case 2: area.getMobEquipment().setLeggings(null); break;
                    case 3: area.getMobEquipment().setBoots(null); break;
                    case 4: area.getMobEquipment().setOffHand(null); break;
                }
                plugin.getAreaManager().saveAreas();
                plugin.getGUIManager().openCustomMobEquipmentMenu((Player) event.getWhoClicked(), area);
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
            case DIAMOND_HELMET:
                plugin.getGUIManager().openCustomMobEquipmentMenu(player, area);
                break;
            case CHEST:
                plugin.getGUIManager().openCustomDropsMenu(player, area);
                break;
            case CRAFTING_TABLE:
                plugin.getGUIManager().openMobStatsMenu(player, area);
                break;
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

    private boolean isHelmet(Material material) {
        return material.name().endsWith("_HELMET");
    }

    private boolean isChestplate(Material material) {
        return material.name().endsWith("_CHESTPLATE");
    }

    private boolean isLeggings(Material material) {
        return material.name().endsWith("_LEGGINGS");
    }

    private boolean isBoots(Material material) {
        return material.name().endsWith("_BOOTS");
    }
}