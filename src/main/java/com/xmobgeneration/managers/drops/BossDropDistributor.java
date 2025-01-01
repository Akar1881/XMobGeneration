package com.xmobgeneration.managers.drops;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class BossDropDistributor {
    private static final double RARE_DROP_THRESHOLD = 5.0;
    private static final double TOP5_DROP_THRESHOLD = 10.0;
    private static final double COMMON_DROP_THRESHOLD = 90.0;

    public static void distributeDrops(List<Map.Entry<UUID, Double>> damagers, 
                                     List<ItemStack> items, 
                                     List<Double> chances,
                                     Map<UUID, Player> playerMap) {
        if (damagers.isEmpty()) return;

        // Single player handling - apply top rank multipliers
        if (damagers.size() == 1) {
            Player player = playerMap.get(damagers.get(0).getKey());
            if (player != null) {
                distributeToSinglePlayer(player, items, chances);
                return;
            }
        }

        // Multiple players handling remains the same
        for (int i = 0; i < items.size(); i++) {
            double chance = chances.get(i);
            ItemStack item = items.get(i);

            if (chance <= RARE_DROP_THRESHOLD) {
                distributeRareDrop(item, chance, damagers.subList(0, Math.min(3, damagers.size())), playerMap);
            } else if (chance <= TOP5_DROP_THRESHOLD) {
                distributeTop5Drop(item, chance, damagers.subList(0, Math.min(5, damagers.size())), playerMap);
            } else if (chance >= COMMON_DROP_THRESHOLD) {
                distributeCommonDrop(item, damagers, playerMap);
            } else {
                distributeNormalDrop(item, chance, damagers, playerMap);
            }
        }
    }

    private static void distributeToSinglePlayer(Player player, List<ItemStack> items, List<Double> chances) {
        Random random = new Random();
        for (int i = 0; i < items.size(); i++) {
            double chance = chances.get(i);
            ItemStack item = items.get(i);

            // Apply appropriate multiplier based on drop type
            double multiplier;
            if (chance <= RARE_DROP_THRESHOLD) {
                multiplier = 3.0; // Top rank rare multiplier
            } else if (chance <= TOP5_DROP_THRESHOLD) {
                multiplier = 2.5; // Top rank top5 multiplier
            } else {
                multiplier = 1.0; // Normal multiplier for common drops
            }

            if (random.nextDouble() * 100 <= chance * multiplier) {
                DropUtil.giveItemToPlayer(player, item.clone(), true);
            }
        }
    }

    private static void distributeRareDrop(ItemStack item, double chance, 
                                         List<Map.Entry<UUID, Double>> topDamagers,
                                         Map<UUID, Player> playerMap) {
        Random random = new Random();
        for (int rank = 0; rank < topDamagers.size(); rank++) {
            Player player = playerMap.get(topDamagers.get(rank).getKey());
            if (player != null) {
                double multiplier = rank == 0 ? 3.0 : rank == 1 ? 2.0 : 1.5;
                if (random.nextDouble() * 100 <= chance * multiplier) {
                    DropUtil.giveItemToPlayer(player, item.clone(), true);
                }
            }
        }
    }

    private static void distributeTop5Drop(ItemStack item, double chance,
                                         List<Map.Entry<UUID, Double>> topDamagers,
                                         Map<UUID, Player> playerMap) {
        Random random = new Random();
        for (int rank = 0; rank < topDamagers.size(); rank++) {
            Player player = playerMap.get(topDamagers.get(rank).getKey());
            if (player != null) {
                double multiplier = getMultiplierForRank(rank);
                if (random.nextDouble() * 100 <= chance * multiplier) {
                    DropUtil.giveItemToPlayer(player, item.clone(), true);
                }
            }
        }
    }

    private static double getMultiplierForRank(int rank) {
        switch (rank) {
            case 0: return 2.5;
            case 1: return 2.0;
            case 2: return 1.5;
            case 3: return 1.25;
            default: return 1.0;
        }
    }

    private static void distributeCommonDrop(ItemStack item, 
                                           List<Map.Entry<UUID, Double>> damagers,
                                           Map<UUID, Player> playerMap) {
        for (Map.Entry<UUID, Double> entry : damagers) {
            Player player = playerMap.get(entry.getKey());
            if (player != null) {
                DropUtil.giveItemToPlayer(player, item.clone(), true);
            }
        }
    }

    private static void distributeNormalDrop(ItemStack item, double chance,
                                           List<Map.Entry<UUID, Double>> damagers,
                                           Map<UUID, Player> playerMap) {
        Random random = new Random();
        if (random.nextDouble() * 100 <= chance) {
            int randomIndex = random.nextInt(damagers.size());
            Player player = playerMap.get(damagers.get(randomIndex).getKey());
            if (player != null) {
                DropUtil.giveItemToPlayer(player, item.clone(), true);
            }
        }
    }
}