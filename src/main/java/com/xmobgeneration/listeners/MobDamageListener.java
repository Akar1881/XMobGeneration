package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDamageListener implements Listener {
    private final XMobGeneration plugin;

    public MobDamageListener(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Only apply custom damage when a mob attacks a player
        if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            if (damager.hasMetadata("mobDamage")) {
                double damage = damager.getMetadata("mobDamage").get(0).asDouble();
                // Set the base damage and ensure it's not modified by other plugins
                event.setDamage(damage);
                event.setCancelled(false);
            }
        }
    }
}