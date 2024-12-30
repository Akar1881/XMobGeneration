package com.xmobgeneration.models;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MobEquipment implements ConfigurationSerializable {
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack offHand;

    public MobEquipment() {
        this.helmet = null;
        this.chestplate = null;
        this.leggings = null;
        this.boots = null;
        this.offHand = null;
    }

    public MobEquipment(Map<String, Object> map) {
        this.helmet = (ItemStack) map.get("helmet");
        this.chestplate = (ItemStack) map.get("chestplate");
        this.leggings = (ItemStack) map.get("leggings");
        this.boots = (ItemStack) map.get("boots");
        this.offHand = (ItemStack) map.get("offHand");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("helmet", helmet);
        map.put("chestplate", chestplate);
        map.put("leggings", leggings);
        map.put("boots", boots);
        map.put("offHand", offHand);
        return map;
    }

    public ItemStack getHelmet() { return helmet; }
    public void setHelmet(ItemStack helmet) { this.helmet = helmet; }

    public ItemStack getChestplate() { return chestplate; }
    public void setChestplate(ItemStack chestplate) { this.chestplate = chestplate; }

    public ItemStack getLeggings() { return leggings; }
    public void setLeggings(ItemStack leggings) { this.leggings = leggings; }

    public ItemStack getBoots() { return boots; }
    public void setBoots(ItemStack boots) { this.boots = boots; }

    public ItemStack getOffHand() { return offHand; }
    public void setOffHand(ItemStack offHand) { this.offHand = offHand; }
}