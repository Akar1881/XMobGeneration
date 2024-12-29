package com.xmobgeneration.models;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class CustomDrops implements ConfigurationSerializable {
    private final List<ItemStack> items;
    private boolean enabled;

    public CustomDrops() {
        this.items = new ArrayList<>();
        this.enabled = false;
    }

    public CustomDrops(Map<String, Object> map) {
        this.items = (List<ItemStack>) map.getOrDefault("items", new ArrayList<>());
        this.enabled = (boolean) map.getOrDefault("enabled", false);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("items", items);
        map.put("enabled", enabled);
        return map;
    }

    public List<ItemStack> getItems() {
        return new ArrayList<>(items);
    }

    public void setItems(List<ItemStack> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void addItem(ItemStack item) {
        items.add(item.clone());
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}