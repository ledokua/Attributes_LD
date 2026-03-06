package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class AxeDamageAttribute extends RangedAttribute {
    public AxeDamageAttribute() {
        super("attribute.name.attributes_ld.axe_damage", 0.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
