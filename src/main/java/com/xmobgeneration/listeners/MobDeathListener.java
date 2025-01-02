package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.SpawnedMob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeathListener implements Listener {
    private final XMobGeneration plugin;

    public MobDeathListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        
        if (killer == null) return;
        
        SpawnedMob spawnedMob = plugin.getSpawnManager().getMobTracker().getMob(entity.getUniqueId());
        if (spawnedMob != null) {
            SpawnArea area = plugin.getAreaManager().getArea(spawnedMob.getAreaName());
            if (area != null) {
                handleXPDistribution(entity, killer, area);
            }
        }
        
        plugin.getSpawnManager().handleMobDeath(entity);
    }

    private void handleXPDistribution(LivingEntity entity, Player killer, SpawnArea area) {
        if (entity.hasMetadata("isBoss")) {
            // Handle boss XP distribution based on damage
            plugin.getXPManager().distributeBossXP(entity.getUniqueId(), area.getXpAmount());
        } else {
            // Award XP for regular mob kill
            plugin.getXPManager().awardXP(killer, area.getXpAmount());
        }
    }
}