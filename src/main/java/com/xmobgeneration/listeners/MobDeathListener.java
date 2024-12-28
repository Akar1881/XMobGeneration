package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.entity.LivingEntity;
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
        plugin.getSpawnManager().handleMobDeath(entity);
    }
}