package net.ledok.attributes_ld.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.player.Player;

public class EntityAttributeRegistry {
    public static void register() {
        FabricDefaultAttributeRegistry.register(
                EntityType.PLAYER,
                Player.createAttributes()
                        .add(AttributeRegistry.DAGGER_DAMAGE)
                        .add(AttributeRegistry.AXE_DAMAGE)
                        .add(AttributeRegistry.SPEAR_DAMAGE)
                        .add(AttributeRegistry.SICKLE_DAMAGE)
                        .add(AttributeRegistry.MACE_DAMAGE)
                        .add(AttributeRegistry.CLAYMORE_DAMAGE)
                        .add(AttributeRegistry.HAMMER_DAMAGE)
                        .add(AttributeRegistry.STAVE_DAMAGE)
                        .add(AttributeRegistry.WAND_DAMAGE)
                        .add(AttributeRegistry.GLAIVE_DAMAGE)
                        .add(AttributeRegistry.SHIELD_BONUS)
        );

        FabricDefaultAttributeRegistry.register(EntityType.BLAZE, Blaze.createAttributes().add(AttributeRegistry.PROJECTILE_DAMAGE));
        FabricDefaultAttributeRegistry.register(EntityType.GHAST, Ghast.createAttributes().add(AttributeRegistry.PROJECTILE_DAMAGE));
        FabricDefaultAttributeRegistry.register(EntityType.BREEZE, Breeze.createAttributes().add(AttributeRegistry.PROJECTILE_DAMAGE));
        FabricDefaultAttributeRegistry.register(EntityType.SHULKER, Shulker.createAttributes().add(AttributeRegistry.PROJECTILE_DAMAGE));
    }
}
