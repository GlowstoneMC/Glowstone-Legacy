package net.glowstone.block;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Coal;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

/**
 * An enum containing an entry for every block describing that block's physical properties.
 */
public enum ItemProperties {

    IRON_SPADE(ItemID.IRON_SPADE, nbtData()),
    IRON_PICKAXE(ItemID.IRON_PICKAXE, nbtData()),
    IRON_AXE(ItemID.IRON_AXE, nbtData()),
    FLINT_AND_STEEL(ItemID.FLINT_AND_STEEL, nbtData()),
    APPLE(ItemID.APPLE),
    BOW(ItemID.BOW),
    ARROW(ItemID.ARROW),
    COAL(ItemID.COAL),
    DIAMOND(ItemID.DIAMOND),
    IRON_INGOT(ItemID.IRON_INGOT),
    GOLD_INGOT(ItemID.GOLD_INGOT),
    IRON_SWORD(ItemID.IRON_SWORD, nbtData()),
    WOOD_SWORD(ItemID.WOOD_SWORD, nbtData()),
    WOOD_SPADE(ItemID.WOOD_SPADE, nbtData()),
    WOOD_PICKAXE(ItemID.WOOD_PICKAXE, nbtData()),
    WOOD_AXE(ItemID.WOOD_AXE, nbtData()),
    STONE_SWORD(ItemID.STONE_SWORD, nbtData()),
    STONE_SPADE(ItemID.STONE_SPADE, nbtData()),
    STONE_PICKAXE(ItemID.STONE_PICKAXE, nbtData()),
    STONE_AXE(ItemID.STONE_AXE, nbtData()),
    DIAMOND_SWORD(ItemID.DIAMOND_SWORD, nbtData()),
    DIAMOND_SPADE(ItemID.DIAMOND_SPADE, nbtData()),
    DIAMOND_PICKAXE(ItemID.DIAMOND_PICKAXE, nbtData()),
    DIAMOND_AXE(ItemID.DIAMOND_AXE, nbtData()),
    STICK(ItemID.STICK),
    BOWL(ItemID.BOWL),
    MUSHROOM_SOUP(ItemID.MUSHROOM_SOUP),
    GOLD_SWORD(ItemID.GOLD_SWORD, nbtData()),
    GOLD_SPADE(ItemID.GOLD_SPADE, nbtData()),
    GOLD_PICKAXE(ItemID.GOLD_PICKAXE, nbtData()),
    GOLD_AXE(ItemID.GOLD_AXE, nbtData()),
    STRING(ItemID.STRING),
    FEATHER(ItemID.FEATHER),
    SULPHUR(ItemID.SULPHUR),
    WOOD_HOE(ItemID.WOOD_HOE, nbtData()),
    STONE_HOE(ItemID.STONE_HOE, nbtData()),
    IRON_HOE(ItemID.IRON_HOE, nbtData()),
    DIAMOND_HOE(ItemID.DIAMOND_HOE, nbtData()),
    GOLD_HOE(ItemID.GOLD_HOE, nbtData()),
    SEEDS(ItemID.SEEDS),
    WHEAT(ItemID.WHEAT),
    BREAD(ItemID.BREAD),
    LEATHER_HELMET(ItemID.LEATHER_HELMET, nbtData()),
    LEATHER_CHESTPLATE(ItemID.LEATHER_CHESTPLATE, nbtData()),
    LEATHER_LEGGINGS(ItemID.LEATHER_LEGGINGS, nbtData()),
    LEATHER_BOOTS(ItemID.LEATHER_BOOTS, nbtData()),
    CHAINMAIL_HELMET(ItemID.CHAINMAIL_HELMET, nbtData()),
    CHAINMAIL_CHESTPLATE(ItemID.CHAINMAIL_CHESTPLATE, nbtData()),
    CHAINMAIL_LEGGINGS(ItemID.CHAINMAIL_LEGGINGS, nbtData()),
    CHAINMAIL_BOOTS(ItemID.CHAINMAIL_BOOTS, nbtData()),
    IRON_HELMET(ItemID.IRON_HELMET, nbtData()),
    IRON_CHESTPLATE(ItemID.IRON_CHESTPLATE, nbtData()),
    IRON_LEGGINGS(ItemID.IRON_LEGGINGS, nbtData()),
    IRON_BOOTS(ItemID.IRON_BOOTS, nbtData()),
    DIAMOND_HELMET(ItemID.DIAMOND_HELMET, nbtData()),
    DIAMOND_CHESTPLATE(311, nbtData()),
    DIAMOND_LEGGINGS(312, nbtData()),
    DIAMOND_BOOTS(313, nbtData()),
    GOLD_HELMET(314, nbtData()),
    GOLD_CHESTPLATE(315, nbtData()),
    GOLD_LEGGINGS(316, nbtData()),
    GOLD_BOOTS(317, nbtData()),
    FLINT(318),
    PORK(319),
    GRILLED_PORK(320),
    PAINTING(321),
    GOLDEN_APPLE(322),
    /* SIGN(323, 1),
    WOOD_DOOR(324, 1),
    BUCKET(325, 1),
    WATER_BUCKET(326, 1),
    LAVA_BUCKET(327, 1),
    MINECART(328, 1),
    SADDLE(329, 1),
    IRON_DOOR(330, 1),
    REDSTONE(331),
    SNOW_BALL(332, 16),
    BOAT(333, 1),
    LEATHER(334),
    MILK_BUCKET(335, 1),
    CLAY_BRICK(336),
    CLAY_BALL(337),
    SUGAR_CANE(338),
    PAPER(339),
    BOOK(340),
    SLIME_BALL(341),
    STORAGE_MINECART(342, 1),
    POWERED_MINECART(343, 1),
    EGG(344, 16),
    COMPASS(345),
    FISHING_ROD(346, nbtData()),
    WATCH(347),
    GLOWSTONE_DUST(348),
    RAW_FISH(349),
    COOKED_FISH(350),
    INK_SACK(351, Dye.class),
    BONE(352),
    SUGAR(353),
    CAKE(354, 1),
    BED(355, 1),
    DIODE(356),
    COOKIE(357),
    MAP(358, 1, MaterialData.class),
    SHEARS(359, nbtData()),
    MELON(360),
    PUMPKIN_SEEDS(361),
    MELON_SEEDS(362),
    RAW_BEEF(363),
    COOKED_BEEF(364),
    RAW_CHICKEN(365),
    COOKED_CHICKEN(366),
    ROTTEN_FLESH(367),
    ENDER_PEARL(368),
    BLAZE_ROD(369),
    GHAST_TEAR(370),
    GOLD_NUGGET(371),
    NETHER_STALK(372),
    POTION(373),
    GLASS_BOTTLE(374),
    SPIDER_EYE(375),
    FERMENTED_SPIDER_EYE(376),
    BLAZE_POWDER(377),
    MAGMA_CREAM(378),
    BREWING_STAND_ITEM(379),
    CAULDRON_ITEM(380),
    EYE_OF_ENDER(381),
    SPECKLED_MELON(382),
    GOLD_RECORD(2256, 1),
    GREEN_RECORD(2257, 1),
    RECORD_3(2258, 1),
    RECORD_4(2259, 1),
    RECORD_5(2260, 1),
    RECORD_6(2261, 1),
    RECORD_7(2262, 1),
    RECORD_8(2263, 1),
    RECORD_9(2264, 1),
    RECORD_10(2265, 1), */
    RECORD_11(2266);

    // -----------------

    private static ItemProperties[] byId = new ItemProperties[32000];

    static {
        for (ItemProperties prop : values()) {
            if (byId.length > prop.id) {
                byId[prop.id] = prop;
            } else {
                byId = Arrays.copyOf(byId, prop.id + 2);
                byId[prop.id] = prop;
            }
        }
    }

    public static ItemProperties get(Material material) {
        return get(material.getId());
    }

    public static ItemProperties get(int id) {
        if (byId.length > id) {
            return byId[id];
        } else {
            return null;
        }
    }

    // -----------------


    private final int id;
    private boolean nbtData;

    private ItemProperties(int id, Property... props) {
        this.id = id;
        
        for (Property p : props) {
            p.apply(this);
        }
    }
    
    public boolean hasNbtData() {
        return nbtData;
    }

    public int getId() {
        return id;
    }
    
    // -----------------
    
    private interface Property {
        void apply(ItemProperties prop);
    }
    
    private static Property nbtData() {
        return new Property() { public void apply(ItemProperties p) {
            p.nbtData = true;
        }};
    }
}
