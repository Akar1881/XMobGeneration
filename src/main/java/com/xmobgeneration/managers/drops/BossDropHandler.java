package com.xmobgeneration.managers.drops;

import com.xmobgeneration.XMobGeneration;
import com.xmobgeneration.models.SpawnArea;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class BossDropHandler {
    private final XMobGeneration plugin;

    public BossDropHandler(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void handleBossDrops(UUID bossId, SpawnArea area) {
        List<Map.Entry<UUID, Double>> damagers = plugin.getBossDamageTracker().getTopDamageDealers(bossId);
        
        if (damagers.isEmpty()) return;

        // Create a map of online players who participated
        Map<UUID, Player> playerMap = new HashMap<>();
        for (Map.Entry<UUID, Double> entry : damagers) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                playerMap.put(entry.getKey(), player);
            }
        }

        if (!playerMap.isEmpty()) {
            BossDropDistributor.distributeDrops(
                damagers,
                area.getCustomDrops().getItems(),
                area.getCustomDrops().getChances(),
                playerMap
            );
        }

        // Clear boss damage data
        plugin.getBossDamageTracker().clearBossData(bossId);
    }
}