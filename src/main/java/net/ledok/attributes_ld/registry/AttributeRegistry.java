package net.ledok.attributes_ld.registry;

import net.ledok.attributes_ld.attribute.*;
import net.ledok.attributes_ld.config.AttributesLdConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AttributeRegistry {
    private static final Map<String, Holder<Attribute>> ATTRIBUTES = new LinkedHashMap<>();
    private static AttributesLdConfig.ConfigData config;

    private static Holder<Attribute> registerAttribute(String name, Attribute attribute) {
        Holder<Attribute> holder = Registry.registerForHolder(
                BuiltInRegistries.ATTRIBUTE,
                ResourceLocation.fromNamespaceAndPath("attributes_ld", name),
                attribute
        );
        ATTRIBUTES.put(name, holder);
        return holder;
    }

    private static void registerAll() {
        registerAttribute("dagger_damage", new DaggerDamageAttribute());
        registerAttribute("axe_damage", new AxeDamageAttribute());
        registerAttribute("spear_damage", new SpearDamageAttribute());
        registerAttribute("sickle_damage", new SickleDamageAttribute());
        registerAttribute("mace_damage", new MaceDamageAttribute());
        registerAttribute("claymore_damage", new ClaymoreDamageAttribute());
        registerAttribute("hammer_damage", new HammerDamageAttribute());
        registerAttribute("stave_damage", new StaveDamageAttribute());
        registerAttribute("wand_damage", new WandDamageAttribute());
        registerAttribute("glaive_damage", new GlaiveDamageAttribute());
        registerAttribute("shield_bonus", new ShieldBonusAttribute());
        registerAttribute("knuckles_damage", new KnucklesDamageAttribute());
        registerAttribute("warrior_weapon_damage", new WarriorWeaponDamageAttribute());
        registerAttribute("rogue_weapon_damage", new RogueWeaponDamageAttribute());
        registerAttribute("paladin_weapon_damage", new PaladinWeaponDamageAttribute());
        registerAttribute("archer_melee_damage", new ArcherMeleeDamageAttribute());
    }

    public static Optional<Holder<Attribute>> get(String name) {
        return Optional.ofNullable(ATTRIBUTES.get(name));
    }

    public static boolean isEnabled(String name) {
        return config != null && config.isEnabled(name);
    }

    public static Map<String, Holder<Attribute>> getEnabledAttributes() {
        Map<String, Holder<Attribute>> enabled = new LinkedHashMap<>();
        for (Map.Entry<String, Holder<Attribute>> entry : ATTRIBUTES.entrySet()) {
            if (isEnabled(entry.getKey())) {
                enabled.put(entry.getKey(), entry.getValue());
            }
        }
        return enabled;
    }

    public static void register() {
        config = AttributesLdConfig.load();
        registerAll();
    }
}
