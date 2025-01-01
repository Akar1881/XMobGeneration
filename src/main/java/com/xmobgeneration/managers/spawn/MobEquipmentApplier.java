package com.xmobgeneration.managers.spawn;

import com.xmobgeneration.models.SpawnArea;
import com.xmobgeneration.models.MobEquipment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

public class MobEquipmentApplier {
    public static void applyEquipment(LivingEntity entity, SpawnArea area) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment != null) {
            MobEquipment mobEquipment = area.getMobEquipment();
            
            equipment.setHelmet(mobEquipment.getHelmet());
            equipment.setChestplate(mobEquipment.getChestplate());
            equipment.setLeggings(mobEquipment.getLeggings());
            equipment.setBoots(mobEquipment.getBoots());
            equipment.setItemInOffHand(mobEquipment.getOffHand());

            // Set drop chances to 0
            equipment.setHelmetDropChance(0);
            equipment.setChestplateDropChance(0);
            equipment.setLeggingsDropChance(0);
            equipment.setBootsDropChance(0);
            equipment.setItemInOffHandDropChance(0);
        }
    }
}