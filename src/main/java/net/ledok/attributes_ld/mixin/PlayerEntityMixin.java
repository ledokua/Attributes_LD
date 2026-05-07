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
import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private static final ResourceLocation ATTRIBUTES_LD_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("attributes_ld", "final_bonus_damage");

    private static Map<TagKey<Item>, String> weaponTagAttributes;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void attributes_ld$updateAttackDamageAttribute(CallbackInfo ci) {
        if (weaponTagAttributes == null) {
            weaponTagAttributes = buildWeaponTagAttributes();
        }
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
        double damage = currentDamage;
        for (Map.Entry<TagKey<Item>, String> entry : weaponTagAttributes.entrySet()) {
            if (itemStack.is(entry.getKey())) {
                Holder<Attribute> attribute = AttributeRegistry.get(entry.getValue()).orElse(null);
                if (attribute == null) {
                    continue;
                }
                AttributeInstance attributeInstance = this.getAttribute(attribute);
                if (attributeInstance != null) {
                    damage = calculateCustomDamage(damage, attributeInstance);
                }
            }
        }
        return damage;
    }

    private static Map<TagKey<Item>, String> buildWeaponTagAttributes() {
        Map<TagKey<Item>, String> map = new LinkedHashMap<>();
        for (String attributeId : AttributeRegistry.getEnabledAttributes().keySet()) {
            if (!AttributeRegistry.isEnabled(attributeId)) {
                continue;
            }
            String tagPath = normalizeAttributeToTagPath(attributeId);
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath("attributes_ld", tagPath);
            map.put(TagKey.create(Registries.ITEM, location), attributeId);
        }
        return map;
    }

    private static String normalizeAttributeToTagPath(String attributeId) {
        if (attributeId.endsWith("_damage")) {
            return attributeId.substring(0, attributeId.length() - "_damage".length());
        }
        if (attributeId.endsWith("_bonus")) {
            return attributeId.substring(0, attributeId.length() - "_bonus".length());
        }
        return attributeId;
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
