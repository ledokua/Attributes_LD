package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class StaveDamageAttribute extends RangedAttribute {
    public StaveDamageAttribute() {
        super("attribute.name.attributes_ld.stave_damage", 0.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
