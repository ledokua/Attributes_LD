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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

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

        // 1. Remove all modifiers previously added by this mod to prevent stacking
        List<ResourceLocation> toRemove = new ArrayList<>();
        for (AttributeModifier modifier : attackDamageAttribute.getModifiers()) {
            if ("attributes_ld".equals(modifier.id().getNamespace())) {
                toRemove.add(modifier.id());
            }
        }
        for (ResourceLocation id : toRemove) {
            attackDamageAttribute.removeModifier(id);
        }

        // 2. Check the held item and find the corresponding custom attribute
        ItemStack mainHandStack = this.getMainHandItem();
        for (Map.Entry<String, Holder<Attribute>> entry : WEAPON_ATTRIBUTES.entrySet()) {
            String weaponType = entry.getKey();
            TagKey<Item> weaponTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("attributes_ld", weaponType));

            if (mainHandStack.is(weaponTag)) {
                AttributeInstance customAttribute = this.getAttribute(entry.getValue());
                if (customAttribute != null) {
                    // 3. Transfer all modifiers from the custom attribute to the vanilla attack damage attribute
                    for (AttributeModifier modifierToTransfer : customAttribute.getModifiers()) {
                        // Create a new, namespaced ID to avoid conflicts
                        ResourceLocation originalId = modifierToTransfer.id();
                        ResourceLocation newId = ResourceLocation.fromNamespaceAndPath("attributes_ld", weaponType + "/" + originalId.getNamespace() + "/" + originalId.getPath());

                        AttributeModifier newModifier = new AttributeModifier(
                                newId,
                                modifierToTransfer.amount(),
                                modifierToTransfer.operation()
                        );
                        attackDamageAttribute.addTransientModifier(newModifier);
                    }
                }
                // Found the weapon, no need to check others
                return;
            }
        }
    }
}
