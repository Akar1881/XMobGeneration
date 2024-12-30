package com.xmobgeneration.models;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class CustomDrops implements ConfigurationSerializable {
    private final List<ItemStack> items;
    private final List<Double> chances;
    private boolean enabled;

    public CustomDrops() {
        this.items = new ArrayList<>();
        this.chances = new ArrayList<>();
        this.enabled = false;
    }

    public CustomDrops(Map<String, Object> map) {
        this.items = (List<ItemStack>) map.getOrDefault("items", new ArrayList<>());
        this.chances = (List<Double>) map.getOrDefault("chances", new ArrayList<>());
        this.enabled = (boolean) map.getOrDefault("enabled", false);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("items", items);
        map.put("chances", chances);
        map.put("enabled", enabled);
        return map;
    }

    public List<ItemStack> getItems() {
        return new ArrayList<>(items);
    }

    public List<Double> getChances() {
        return new ArrayList<>(chances);
    }

    public void setItems(List<ItemStack> items, List<Double> chances) {
        this.items.clear();
        this.chances.clear();
        this.items.addAll(items);
        this.chances.addAll(chances);
    }

    public void addItem(ItemStack item, double chance) {
        items.add(item.clone());
        chances.add(Math.min(100.0, Math.max(0.0, chance)));
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            chances.remove(index);
        }
    }

    public double getChance(int index) {
        return index >= 0 && index < chances.size() ? chances.get(index) : 0.0;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}