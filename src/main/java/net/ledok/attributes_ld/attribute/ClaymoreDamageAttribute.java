package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ClaymoreDamageAttribute extends RangedAttribute {
    public ClaymoreDamageAttribute() {
        super("attribute.name.attributes_ld.claymore_damage", 100.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
