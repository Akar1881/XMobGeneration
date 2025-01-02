package com.xmobgeneration.managers;

import com.xmobgeneration.XMobGeneration;
import org.bukkit.entity.Player;
import java.util.*;

public class XPManager {
    private final XMobGeneration plugin;

    public XPManager(XMobGeneration plugin) {
        this.plugin = plugin;
    }

    public void awardXP(Player player, int amount) {
        player.giveExp(amount);
        player.sendMessage(plugin.getConfigManager().getMessage("xp-earned")
            .replace("%amount%", String.valueOf(amount)));
    }

    public void distributeBossXP(UUID bossId, int totalXP) {
        List<Map.Entry<UUID, Double>> damagers = plugin.getBossDamageTracker()
            .getTopDamageDealers(bossId);
            
        if (damagers.isEmpty()) return;

        // Calculate total damage
        double totalDamage = damagers.stream()
            .mapToDouble(Map.Entry::getValue)
            .sum();

        // Distribute XP based on damage percentage
        for (Map.Entry<UUID, Double> entry : damagers) {
            Player player = plugin.getServer().getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                double percentage = entry.getValue() / totalDamage;
                int xpShare = (int) Math.ceil(totalXP * percentage);
                awardXP(player, xpShare);
            }
        }
    }
}