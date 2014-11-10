package net.glowstone.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExternalEntityProvider {

    private static final Map<String, Class<? extends Entity>> externalClassMap = new LinkedHashMap<>();

    public static void registerCustomEntity(Class<? extends Entity> clazz, String identifier) {
        Validate.notNull(clazz, "Cannot register a null class");
        Validate.notNull(identifier, "Cannot register a null identifier");
        Validate.isTrue(!identifier.isEmpty(), "Cannot register an empty string as an identifier");
        if (externalClassMap.containsKey(identifier)) {
            throw new IllegalArgumentException("There is already an identifier registered with the following name:" + identifier);
        }
        ExternalEntityProvider.externalClassMap.put(identifier, clazz);
    }

    public static boolean isRegistered(String identifier) {
        Validate.notNull(identifier, "Cannot check a null identifier");
        return externalClassMap.containsKey(identifier);
    }
}
