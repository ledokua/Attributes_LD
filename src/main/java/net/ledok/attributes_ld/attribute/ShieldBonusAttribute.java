package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ShieldBonusAttribute extends RangedAttribute {
    public ShieldBonusAttribute() {
        super("attribute.name.attributes_ld.shield_bonus", 0.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
