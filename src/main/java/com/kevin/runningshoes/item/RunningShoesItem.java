package com.kevin.runningshoes.item;

import com.kevin.runningshoes.RunningShoesMod;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;

public class RunningShoesItem extends Item {
    public static final RunningShoesItem INSTANCE = new RunningShoesItem(createSettings());

    private static Item.Properties createSettings() {
        ItemAttributeModifiers attributes = ArmorMaterials.LEATHER
                .createAttributes(ArmorType.BOOTS)
                .withModifierAdded(
                        Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(RunningShoesMod.MOD_ID, "running_shoes_speed_bonus"),
                                0.05,
                                Operation.ADD_MULTIPLIED_TOTAL
                        ),
                        EquipmentSlotGroup.FEET
                );

        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, RunningShoesMod.id("running_shoes")))
                .humanoidArmor(ArmorMaterials.LEATHER, ArmorType.BOOTS)
                .durability(300)
                .attributes(attributes);
    }

    public RunningShoesItem(Item.Properties properties) {
        super(properties);
    }

    public static void register() {
        Registry.register(
                BuiltInRegistries.ITEM,
                RunningShoesMod.id("running_shoes"),
                INSTANCE
        );
        RunningShoesMod.LOGGER.info("Registered Running Shoes item");
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (!(entity instanceof Player player)
                || slot != EquipmentSlot.FEET
                || !player.getItemBySlot(EquipmentSlot.FEET).is(this)
                || !player.isSprinting()) {
            return;
        }

        player.addEffect(new MobEffectInstance(MobEffects.SPEED, 20, 1, false, false));

        if (level.getGameTime() % 60 == 0) {
            stack.hurtAndBreak(1, player, EquipmentSlot.FEET);
        }

        level.sendParticles(
                ParticleTypes.CLOUD,
                player.getX(),
                player.getY(),
                player.getZ(),
                1,
                0.0,
                0.0,
                0.0,
                0.0
        );
    }
}
