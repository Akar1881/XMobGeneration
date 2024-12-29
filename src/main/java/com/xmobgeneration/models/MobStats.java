package com.xmobgeneration.models;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.util.HashMap;
import java.util.Map;

public class MobStats implements ConfigurationSerializable {
    private String mobName;
    private double health;
    private double damage;
    private int level;
    private boolean showName;

    public MobStats() {
        this.mobName = "Monster";
        this.health = 20.0;
        this.damage = 2.0;
        this.level = 1;
        this.showName = false;
    }

    public MobStats(Map<String, Object> map) {
        this.mobName = (String) map.getOrDefault("mobName", "Monster");
        this.health = (double) map.getOrDefault("health", 20.0);
        this.damage = (double) map.getOrDefault("damage", 2.0);
        this.level = (int) map.getOrDefault("level", 1);
        this.showName = (boolean) map.getOrDefault("showName", false);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("mobName", mobName);
        map.put("health", health);
        map.put("damage", damage);
        map.put("level", level);
        map.put("showName", showName);
        return map;
    }

    public String getMobName() {
        return mobName;
    }

    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = Math.max(1.0, health);
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = Math.max(0.0, damage);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public String getDisplayName() {
        return String.format("§7[Lv.%d] §f%s §c[%.1f❤]", level, mobName, health);
    }
}