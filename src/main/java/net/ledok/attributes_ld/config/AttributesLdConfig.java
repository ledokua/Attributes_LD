package net.ledok.attributes_ld.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AttributesLdConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("attributes_ld.json");

    private AttributesLdConfig() {
    }

    public static ConfigData load() {
        ConfigData defaults = ConfigData.defaults();
        if (!Files.exists(CONFIG_PATH)) {
            save(defaults);
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            ConfigData loaded = GSON.fromJson(reader, ConfigData.class);
            if (loaded == null || loaded.enabledAttributes == null) {
                save(defaults);
                return defaults;
            }
            defaults.enabledAttributes.forEach(loaded.enabledAttributes::putIfAbsent);
            return loaded;
        } catch (IOException | JsonParseException e) {
            save(defaults);
            return defaults;
        }
    }

    public static void save(ConfigData data) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public static final class ConfigData {
        public Map<String, Boolean> enabledAttributes = new LinkedHashMap<>();

        public boolean isEnabled(String attributeId) {
            return enabledAttributes.getOrDefault(attributeId, false);
        }

        public static ConfigData defaults() {
            ConfigData data = new ConfigData();
            data.enabledAttributes.put("dagger_damage", true);
            data.enabledAttributes.put("axe_damage", true);
            data.enabledAttributes.put("spear_damage", true);
            data.enabledAttributes.put("sickle_damage", true);
            data.enabledAttributes.put("mace_damage", true);
            data.enabledAttributes.put("claymore_damage", true);
            data.enabledAttributes.put("hammer_damage", true);
            data.enabledAttributes.put("stave_damage", true);
            data.enabledAttributes.put("wand_damage", true);
            data.enabledAttributes.put("glaive_damage", true);
            data.enabledAttributes.put("shield_bonus", true);
            data.enabledAttributes.put("knuckles_damage", true);
            data.enabledAttributes.put("warrior_weapon_damage", true);
            data.enabledAttributes.put("rogue_weapon_damage", true);
            data.enabledAttributes.put("paladin_weapon_damage", true);
            data.enabledAttributes.put("archer_melee_damage", true);
            return data;
        }
    }
}
