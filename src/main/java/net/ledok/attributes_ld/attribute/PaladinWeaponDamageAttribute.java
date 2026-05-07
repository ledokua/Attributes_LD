package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class PaladinWeaponDamageAttribute extends RangedAttribute {
    public PaladinWeaponDamageAttribute() {
        super("attribute.name.attributes_ld.paladin_weapon_damage", 0.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
