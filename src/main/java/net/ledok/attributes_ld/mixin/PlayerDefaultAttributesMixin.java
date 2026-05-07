package net.ledok.attributes_ld.mixin;

import net.ledok.attributes_ld.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerDefaultAttributesMixin {

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void attributes_ld$appendCustomAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        AttributeSupplier.Builder builder = cir.getReturnValue();
        for (var attribute : AttributeRegistry.getEnabledAttributes().values()) {
            builder.add(attribute);
        }
    }
}
