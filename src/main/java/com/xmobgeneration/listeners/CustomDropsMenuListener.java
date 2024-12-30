package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomDropsMenuListener implements Listener {
    private final XMobGeneration plugin;

    public CustomDropsMenuListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (title.startsWith("§8Custom Drops - ")) {
            String areaName = title.substring("§8Custom Drops - ".length());
            SpawnArea area = plugin.getAreaManager().getArea(areaName);
            if (area != null) {
                plugin.getGUIManager().getCustomDropsMenu().handleInventoryClose(event, area);
            }
        }
    }
}