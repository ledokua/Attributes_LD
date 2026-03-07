package net.ledok.attributes_ld.mixin;

import net.ledok.attributes_ld.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private static final ResourceLocation ATTRIBUTES_LD_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("attributes_ld", "final_bonus_damage");

    private static final Map<String, Holder<Attribute>> WEAPON_ATTRIBUTES = Map.ofEntries(
            Map.entry("dagger", AttributeRegistry.DAGGER_DAMAGE),
            Map.entry("axe", AttributeRegistry.AXE_DAMAGE),
            Map.entry("spear", AttributeRegistry.SPEAR_DAMAGE),
            Map.entry("sickle", AttributeRegistry.SICKLE_DAMAGE),
            Map.entry("mace", AttributeRegistry.MACE_DAMAGE),
            Map.entry("claymore", AttributeRegistry.CLAYMORE_DAMAGE),
            Map.entry("hammer", AttributeRegistry.HAMMER_DAMAGE),
            Map.entry("stave", AttributeRegistry.STAVE_DAMAGE),
            Map.entry("wand", AttributeRegistry.WAND_DAMAGE),
            Map.entry("glaive", AttributeRegistry.GLAIVE_DAMAGE),
            Map.entry("shield", AttributeRegistry.SHIELD_BONUS)
    );

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void attributes_ld$updateAttackDamageAttribute(CallbackInfo ci) {
        AttributeInstance attackDamageAttribute = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttribute == null) {
            return;
        }

        // 1. Remove our modifier from the previous tick
        attackDamageAttribute.removeModifier(ATTRIBUTES_LD_MODIFIER_ID);

        // 2. Get the player's base attack damage for this tick
        double baseAttackDamage = attackDamageAttribute.getValue();
        double finalDamage = baseAttackDamage;

        // 3. Apply custom formula for main hand
        ItemStack mainHandStack = this.getMainHandItem();
        finalDamage = applyCustomFormula(mainHandStack, finalDamage);

        // 4. Apply custom formula for off-hand
        ItemStack offHandStack = this.getOffhandItem();
        finalDamage = applyCustomFormula(offHandStack, finalDamage);

        // 5. Calculate the bonus and add it back as a single flat modifier
        double bonusDamage = finalDamage - baseAttackDamage;
        if (Math.abs(bonusDamage) > 1.0E-7) { // Avoid adding zero modifiers
            AttributeModifier modifier = new AttributeModifier(
                    ATTRIBUTES_LD_MODIFIER_ID,
                    bonusDamage,
                    AttributeModifier.Operation.ADD_VALUE
            );
            attackDamageAttribute.addTransientModifier(modifier);
        }
    }

    private double applyCustomFormula(ItemStack itemStack, double currentDamage) {
        for (Map.Entry<String, Holder<Attribute>> entry : WEAPON_ATTRIBUTES.entrySet()) {
            String weaponType = entry.getKey();
            TagKey<Item> weaponTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("attributes_ld", weaponType));

            if (itemStack.is(weaponTag)) {
                AttributeInstance attributeInstance = this.getAttribute(entry.getValue());
                if (attributeInstance != null) {
                    return calculateCustomDamage(currentDamage, attributeInstance);
                }
            }
        }
        return currentDamage;
    }

    private double calculateCustomDamage(double baseDamage, AttributeInstance attributeInstance) {
        Collection<AttributeModifier> modifiers = attributeInstance.getModifiers();

        double a = modifiers.stream()
                .filter(m -> m.operation() == AttributeModifier.Operation.ADD_VALUE)
                .mapToDouble(AttributeModifier::amount)
                .sum();

        double b = modifiers.stream()
                .filter(m -> m.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .mapToDouble(AttributeModifier::amount)
                .sum();

        double t_product = 1.0;
        for (AttributeModifier modifier : modifiers) {
            if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                t_product *= (1.0 + modifier.amount());
            }
        }

        // (atk * (1 + b) + a) * t_product
        return (baseDamage * (1.0 + b) + a) * t_product;
    }
}