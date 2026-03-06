package net.ledok.attributes_ld;

import net.fabricmc.api.ModInitializer;
import net.ledok.attributes_ld.registry.AttributeRegistry;
import net.ledok.attributes_ld.registry.EntityAttributeRegistry;

public class AttributesLdMod implements ModInitializer {

    @Override
    public void onInitialize() {
        AttributeRegistry.register();
        EntityAttributeRegistry.register();
    }
}
