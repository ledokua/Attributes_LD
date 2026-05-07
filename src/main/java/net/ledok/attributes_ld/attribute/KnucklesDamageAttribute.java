package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class KnucklesDamageAttribute extends RangedAttribute {
    public KnucklesDamageAttribute() {
        super("attribute.name.attributes_ld.knuckles_damage", 0.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
