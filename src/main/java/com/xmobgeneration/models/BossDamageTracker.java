package com.xmobgeneration.models;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class BossDamageTracker {
    private final Map<UUID, Map<UUID, Double>> bossDamageMap = new HashMap<>(); // Boss UUID -> {Player UUID -> Damage}
    private static final int TOP_DAMAGE_THRESHOLD = 3; // Top 3 players get rare items

    public void recordDamage(UUID bossId, Player player, double damage) {
        Map<UUID, Double> playerDamage = bossDamageMap.computeIfAbsent(bossId, k -> new HashMap<>());
        playerDamage.merge(player.getUniqueId(), damage, Double::sum);
    }

    public List<Map.Entry<UUID, Double>> getTopDamageDealers(UUID bossId) {
        Map<UUID, Double> damageMap = bossDamageMap.getOrDefault(bossId, new HashMap<>());
        List<Map.Entry<UUID, Double>> sortedDamage = new ArrayList<>(damageMap.entrySet());
        sortedDamage.sort(Map.Entry.<UUID, Double>comparingByValue().reversed());
        return sortedDamage;
    }

    public void displayDamageLeaderboard(UUID bossId, String bossName) {
        List<Map.Entry<UUID, Double>> topDealers = getTopDamageDealers(bossId);
        
        Bukkit.broadcastMessage("§6=== " + bossName + " Damage Leaderboard ===");
        for (int i = 0; i < topDealers.size(); i++) {
            Map.Entry<UUID, Double> entry = topDealers.get(i);
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                Bukkit.broadcastMessage(String.format("§e#%d §f%s §7- §c%.1f damage", 
                    i + 1, player.getName(), entry.getValue()));
            }
        }
    }

    public boolean isTopDamageDealer(UUID bossId, UUID playerId) {
        List<Map.Entry<UUID, Double>> topDealers = getTopDamageDealers(bossId);
        for (int i = 0; i < Math.min(TOP_DAMAGE_THRESHOLD, topDealers.size()); i++) {
            if (topDealers.get(i).getKey().equals(playerId)) {
                return true;
            }
        }
        return false;
    }

    public void clearBossData(UUID bossId) {
        bossDamageMap.remove(bossId);
    }
}