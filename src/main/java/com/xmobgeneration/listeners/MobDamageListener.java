package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDamageListener implements Listener {
    private final XMobGeneration plugin;

    public MobDamageListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            if (damager.hasMetadata("mobDamage")) {
                double damage = damager.getMetadata("mobDamage").get(0).asDouble();
                event.setDamage(damage);
            }
        }
    }
}