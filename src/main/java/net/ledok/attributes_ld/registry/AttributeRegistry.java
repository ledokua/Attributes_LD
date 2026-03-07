package net.ledok.attributes_ld.registry;

import net.ledok.attributes_ld.attribute.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeRegistry {

    public static final Holder<Attribute> DAGGER_DAMAGE = registerAttribute("dagger_damage", new DaggerDamageAttribute());
    public static final Holder<Attribute> AXE_DAMAGE = registerAttribute("axe_damage", new AxeDamageAttribute());
    public static final Holder<Attribute> SPEAR_DAMAGE = registerAttribute("spear_damage", new SpearDamageAttribute());
    public static final Holder<Attribute> SICKLE_DAMAGE = registerAttribute("sickle_damage", new SickleDamageAttribute());
    public static final Holder<Attribute> MACE_DAMAGE = registerAttribute("mace_damage", new MaceDamageAttribute());
    public static final Holder<Attribute> CLAYMORE_DAMAGE = registerAttribute("claymore_damage", new ClaymoreDamageAttribute());
    public static final Holder<Attribute> HAMMER_DAMAGE = registerAttribute("hammer_damage", new HammerDamageAttribute());
    public static final Holder<Attribute> STAVE_DAMAGE = registerAttribute("stave_damage", new StaveDamageAttribute());
    public static final Holder<Attribute> WAND_DAMAGE = registerAttribute("wand_damage", new WandDamageAttribute());
    public static final Holder<Attribute> GLAIVE_DAMAGE = registerAttribute("glaive_damage", new GlaiveDamageAttribute());
    public static final Holder<Attribute> SHIELD_BONUS = registerAttribute("shield_bonus", new ShieldBonusAttribute());
    public static final Holder<Attribute> PROJECTILE_DAMAGE = registerAttribute("projectile_damage", new ProjectileDamageAttribute());


    private static Holder<Attribute> registerAttribute(String name, Attribute attribute) {
        return Registry.registerForHolder(
                BuiltInRegistries.ATTRIBUTE,
                ResourceLocation.fromNamespaceAndPath("attributes_ld", name),
                attribute
        );
    }

    public static void register() {
        // All attributes are registered via their static initializers.
        // This method is kept to ensure the class is loaded.
    }
}
