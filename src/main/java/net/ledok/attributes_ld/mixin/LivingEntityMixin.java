package net.ledok.attributes_ld.mixin;

import net.ledok.attributes_ld.registry.AttributeRegistry;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(
            method = "hurt",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float attributes_ld$applyProjectileDamage(float originalAmount, DamageSource source) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            Entity directEntity = source.getDirectEntity();
            if (directEntity instanceof Projectile) {
                Entity attacker = ((Projectile) directEntity).getOwner();
                if (attacker instanceof LivingEntity) {
                    AttributeInstance projectileDamageAttribute = ((LivingEntity) attacker).getAttribute(AttributeRegistry.PROJECTILE_DAMAGE);
                    if (projectileDamageAttribute != null) {
                        return originalAmount + (float) projectileDamageAttribute.getValue();
                    }
                }
            }
        }
        return originalAmount;
    }
}
