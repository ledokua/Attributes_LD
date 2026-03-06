package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class SpearDamageAttribute extends RangedAttribute {
    public SpearDamageAttribute() {
        super("attribute.name.attributes_ld.spear_damage", 0.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
