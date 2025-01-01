package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.models.SpawnArea;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import com.xmobgeneration.XMobGeneration;

public class MobStatApplier {
    public static void applyStats(LivingEntity entity, SpawnArea area) {
        if (area.getMobStats().isShowName()) {
            entity.setCustomName(area.getMobStats().getDisplayName());
            entity.setCustomNameVisible(true);
        }
        
        entity.setMaxHealth(area.getMobStats().getHealth());
        entity.setHealth(area.getMobStats().getHealth());
        entity.setMetadata("mobDamage", 
            new FixedMetadataValue(XMobGeneration.getInstance(), area.getMobStats().getDamage()));
    }
}