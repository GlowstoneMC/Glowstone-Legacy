package net.glowstone.block;

import org.bukkit.Material;

/**
 * MaterialValueManager provides easily access to {@link Material} related values (e.g. block hardness).
 */
public interface MaterialValueManager {
    /**
     * Returns the {@link ValueCollection} for the given material.
     * If there aren't concrete values for this material, a {@link ValueCollection} with default values will be returned.
     * @param material The material to look for
     * @return a {@link ValueCollection} object with values for the given material or default values
     */
    ValueCollection getValues(Material material);

    public interface ValueCollection {
        /**
         * Returns the hardness-component of this value.
         * @return the hardness (or Float.MAX_VALUE for infinity hardness)
         */
        float getHardness();

        /**
         * Returns the blast resistance-component of this value.
         * @return the blast resistance
         */
        float getBlastResistance();
    }
}
