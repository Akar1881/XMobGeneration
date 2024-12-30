package com.xmobgeneration.mythicmobs;

import com.xmobgeneration.XMobGeneration;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MythicMobsManager {
    private final XMobGeneration plugin;
    private boolean mythicMobsEnabled;

    public MythicMobsManager(XMobGeneration plugin) {
        this.plugin = plugin;
        this.mythicMobsEnabled = plugin.getServer().getPluginManager().isPluginEnabled("MythicMobs");
    }

    public boolean isMythicMob(String mobType) {
        if (!mythicMobsEnabled) return false;
        return MythicBukkit.inst().getMobManager().getMythicMob(mobType).isPresent();
    }

    public Entity spawnMythicMob(String mobType, Location location, int level) {
        if (!mythicMobsEnabled) return null;

        Optional<MythicMob> mythicMob = MythicBukkit.inst().getMobManager().getMythicMob(mobType);
        if (!mythicMob.isPresent()) return null;

        ActiveMob activeMob = mythicMob.get().spawn(BukkitAdapter.adapt(location), level);
        return activeMob != null ? activeMob.getEntity().getBukkitEntity() : null;
    }

    public List<String> getMythicMobTypes() {
        List<String> mobTypes = new ArrayList<>();
        if (mythicMobsEnabled) {
            MythicBukkit.inst().getMobManager().getMobNames().forEach(mobTypes::add);
        }
        return mobTypes;
    }

    public boolean isMythicMobsEnabled() {
        return mythicMobsEnabled;
    }
}