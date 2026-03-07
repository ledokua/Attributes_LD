package net.ledok.attributes_ld.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ProjectileDamageAttribute extends RangedAttribute {
    public ProjectileDamageAttribute() {
        super("attribute.name.attributes_ld.projectile_damage", 0.0, 0.0, 10240.0);
        this.setSyncable(true);
    }
}
