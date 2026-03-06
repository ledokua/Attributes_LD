package net.ledok.attributes_ld.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
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
    }
}
