package net.glowstone.constants;

import org.bukkit.Material;

/**
 * Maps attack damage values
 */
public final class AttackDamage {

    private AttackDamage() {
    }

    /**
     * Gets the damage an item in-hand would cause without added benefits.
     *
     * @param material the item type
     * @return the raw damage caused by that item
     */
    public static float getMeleeDamage(Material material) {
        if (material == null) return 0.0f;

        switch (material) {
            case WOOD_SPADE:
            case GOLD_SPADE:
                return 2.0f;
            case STONE_SPADE:
            case WOOD_PICKAXE:
            case GOLD_PICKAXE:
                return 3.0f;
            case IRON_SPADE:
            case STONE_PICKAXE:
            case WOOD_AXE:
            case GOLD_AXE:
                return 4.0f;
            case DIAMOND_SPADE:
            case IRON_PICKAXE:
            case STONE_AXE:
            case WOOD_SWORD:
            case GOLD_SWORD:
                return 5.0f;
            case DIAMOND_PICKAXE:
            case IRON_AXE:
            case STONE_SWORD:
                return 6.0f;
            case DIAMOND_AXE:
            case IRON_SWORD:
                return 7.0f;
            case DIAMOND_SWORD:
                return 8.0f;
            default:
                return 1.0f;
        }
    }

    /**
     * Gets the damage an in-hand item would cause without added benefits upon
     * a critical hit.
     *
     * @param material the item type
     * @return the raw damage caused by that item during a critical hit
     */
    public static float getCriticalMeleeDamage(Material material) {
        float raw = getMeleeDamage(material);
        return raw * 1.5f;
    }

    /**
     * Gets the durability loss of the supplied type for a successful hit. This
     * assumes that the supplied type was used for a melee attack.
     *
     * @param material the item type
     * @return the durability points lost, or 0
     */
    public static short getMeleeDurabilityLoss(Material material) {
        short loss = 0;
        if (material != null) {
            switch (material) {
                case WOOD_AXE:
                case GOLD_AXE:
                case STONE_AXE:
                case DIAMOND_AXE:
                case WOOD_PICKAXE:
                case GOLD_PICKAXE:
                case IRON_PICKAXE:
                case DIAMOND_PICKAXE:
                case WOOD_SPADE:
                case GOLD_SPADE:
                case IRON_SPADE:
                case DIAMOND_SPADE:
                    loss = 2;
                    break;
                case WOOD_SWORD:
                case GOLD_SWORD:
                case IRON_SWORD:
                case DIAMOND_SWORD:
                    loss = 1;
                    break;
                default:
                    break;
            }
        }
        return loss;
    }

    // TODO: Other methods for stuff like lava contact, fire, arrows, etc

}
