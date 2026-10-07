package com.kevin.runningshoes.item;

import com.kevin.runningshoes.RunningShoesMod;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

public class RunningShoesItem extends ArmorItem {

    // The single instance of the item
    public static final RunningShoesItem INSTANCE =
            new RunningShoesItem(new Item.Settings().maxDamage(300));

    public RunningShoesItem(Item.Settings settings) {
        super(ArmorMaterials.LEATHER, EquipmentSlot.FEET,
                settings.attributeModifiers(
                        ArmorItem.createAttributeModifiers(
                                ArmorMaterials.LEATHER,
                                EquipmentSlot.FEET,
                                settings
                        ).with(
                                net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED,
                                new net.minecraft.entity.attribute.EntityAttributeModifier(
                                        java.util.UUID.fromString("11111111-2222-3333-4444-555555555555"),
                                        "running_shoes_speed_bonus",
                                        0.05,
                                        net.minecraft.entity.attribute.EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                                )
                        )
                )
        );
    }

    // Called from RunningShoesMod.onInitialize()
    public static void register() {
        Registry.register(
                Registries.ITEM,
                RunningShoesMod.id("running_shoes"),
                INSTANCE
        );
        RunningShoesMod.LOGGER.info("Registered Running Shoes item");
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player)) return;

        // Only apply effects if actually worn
        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);
        if (!boots.isOf(this)) return;

        if (player.isSprinting()) {

            // Speed boost while sprinting
            player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.SPEED,
                    20, // 1 second
                    1,  // Speed II
                    false,
                    false
            ));

            // Durability drain every 3 seconds
            if (!world.isClient && world.getTime() % 60 == 0) {
                stack.damage(1, player, p -> p.sendEquipmentBreakStatus(EquipmentSlot.FEET));
            }

            // Particle trail
            world.addParticle(
                    ParticleTypes.CLOUD,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    0.0, 0.0, 0.0
            );
        }
    }
}
