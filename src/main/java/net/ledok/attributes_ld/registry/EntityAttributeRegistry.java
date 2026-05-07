package net.ledok.attributes_ld.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class EntityAttributeRegistry {
    public static void register() {
        var builder = Player.createAttributes();
        for (var attribute : AttributeRegistry.getEnabledAttributes().values()) {
            builder.add(attribute);
        }
        FabricDefaultAttributeRegistry.register(EntityType.PLAYER, builder);
    }
}
