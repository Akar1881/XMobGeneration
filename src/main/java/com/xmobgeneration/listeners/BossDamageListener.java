package com.xmobgeneration.listeners;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.managers.drops.BossDropHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class BossDamageListener implements Listener {
    private final XMobGeneration plugin;
    private final BossDropHandler dropHandler;

    public BossDamageListener(XMobGeneration plugin) {
        this.plugin = plugin;
        this.dropHandler = new BossDropHandler(plugin);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!event.getEntity().hasMetadata("isBoss")) return;

        Player player = (Player) event.getDamager();
        plugin.getBossDamageTracker().recordDamage(
            event.getEntity().getUniqueId(),
            player,
            event.getFinalDamage()
        );
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!entity.hasMetadata("isBoss")) return;

        String areaName = entity.getMetadata("areaName").get(0).asString();
        plugin.getBossDamageTracker().displayDamageLeaderboard(entity.getUniqueId(), entity.getCustomName());
        
        // Clear default drops and handle custom drops
        event.getDrops().clear();
        dropHandler.handleBossDrops(entity.getUniqueId(), plugin.getAreaManager().getArea(areaName));

        // Trigger boss death handling
        plugin.getSpawnManager().handleMobDeath(entity);
        
        // Despawn all mobs in the boss area
        plugin.getSpawnManager().despawnBossAreaMobs(areaName);
    }
}