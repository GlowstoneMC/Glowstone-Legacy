package net.glowstone.block;

import org.bukkit.Material;

import java.util.EnumMap;

/**
 * An enum containing an entry for every item describing that block's physical properties.
 */
public enum ItemProperties {

    IRON_SPADE(tool()),
    IRON_PICKAXE(tool()),
    IRON_AXE(tool()),
    FLINT_AND_STEEL(interact()),
    APPLE(food(5)),
    BOW(tool()),
    ARROW(),
    COAL(),
    DIAMOND(),
    IRON_INGOT(),
    GOLD_INGOT(),
    IRON_SWORD(tool()),
    WOOD_SWORD(tool()),
    WOOD_SPADE(tool()),
    WOOD_PICKAXE(tool()),
    WOOD_AXE(tool()),
    STONE_SWORD(tool()),
    STONE_SPADE(tool()),
    STONE_PICKAXE(tool()),
    STONE_AXE(tool()),
    DIAMOND_SWORD(tool()),
    DIAMOND_SPADE(tool()),
    DIAMOND_PICKAXE(tool()),
    DIAMOND_AXE(tool()),
    STICK(),
    BOWL(),
    MUSHROOM_SOUP(food(10)),
    GOLD_SWORD(tool()),
    GOLD_SPADE(tool()),
    GOLD_PICKAXE(tool()),
    GOLD_AXE(tool()),
    STRING(),
    FEATHER(),
    SULPHUR(),
    WOOD_HOE(tool()),
    STONE_HOE(tool()),
    IRON_HOE(tool()),
    DIAMOND_HOE(tool()),
    GOLD_HOE(tool()),
    SEEDS(place(Material.WHEAT)),
    WHEAT(),
    BREAD(food(5)),
    LEATHER_HELMET(),
    LEATHER_CHESTPLATE(),
    LEATHER_LEGGINGS(),
    LEATHER_BOOTS(),
    CHAINMAIL_HELMET(),
    CHAINMAIL_CHESTPLATE(),
    CHAINMAIL_LEGGINGS(),
    CHAINMAIL_BOOTS(),
    IRON_HELMET(),
    IRON_CHESTPLATE(),
    IRON_LEGGINGS(),
    IRON_BOOTS(),
    DIAMOND_HELMET(),
    DIAMOND_CHESTPLATE(),
    DIAMOND_LEGGINGS(),
    DIAMOND_BOOTS(),
    GOLD_HELMET(),
    GOLD_CHESTPLATE(),
    GOLD_LEGGINGS(),
    GOLD_BOOTS(),
    FLINT(),
    PORK(food(3)),
    GRILLED_PORK(food(8)),
    PAINTING(),
    GOLDEN_APPLE(food(20)),
    SIGN(place(Material.SIGN_POST)),
    WOOD_DOOR(place(Material.WOODEN_DOOR)),
    BUCKET(interact()),
    WATER_BUCKET(interact(), place(Material.WATER)),
    LAVA_BUCKET(interact(), place(Material.LAVA)),
    MINECART(), // todo: entity placing
    SADDLE(interact()),
    IRON_DOOR(place(Material.IRON_DOOR_BLOCK)),
    REDSTONE(place(Material.REDSTONE_WIRE)),
    SNOW_BALL(interact()),
    BOAT(), //todo: entity placing
    LEATHER(),
    MILK_BUCKET(interact()),
    CLAY_BRICK(),
    CLAY_BALL(),
    SUGAR_CANE(place(Material.SUGAR_CANE_BLOCK)),
    PAPER(),
    BOOK(),
    SLIME_BALL(),
    STORAGE_MINECART(interact()), //todo: entity placing
    POWERED_MINECART(interact()), //todo: entity placing
    EGG(interact()),
    COMPASS(),
    FISHING_ROD(interact()),
    WATCH(),
    GLOWSTONE_DUST(),
    RAW_FISH(food(3)),
    COOKED_FISH(food(5)),
    INK_SACK(interact()),
    BONE(),
    SUGAR(),
    CAKE(place(Material.CAKE_BLOCK)),
    BED(place(Material.BED_BLOCK)),
    DIODE(place(Material.DIODE_BLOCK_OFF)),
    COOKIE(food(1)),
    MAP(interact()),
    SHEARS(tool()),
    GOLD_RECORD(interact()),
    GREEN_RECORD(interact());

    // -----------------

    private static EnumMap<Material, ItemProperties> materialMap = new EnumMap<Material, ItemProperties>(Material.class);

    static {
        for (ItemProperties prop : values()) {
            materialMap.put(prop.material, prop);
        }
    }

    public static ItemProperties get(Material material) {
        return materialMap.get(material);
    }

    public static ItemProperties get(int id) {
        return materialMap.get(Material.getMaterial(id));
    }

    // -----------------

    private final Material material;
    private int heals = 0;
    private boolean tool = false;
    private boolean interact = false;
    private Material placeBlock;

    private ItemProperties(Property... props) {
        material = Material.getMaterial(toString());
        
        for (Property p : props)
            p.apply(this);
    }
    
    public boolean isTool() {
        return tool;
    }

    public int heals() {
        return heals;
    }
    
    public boolean isInteractable() {
        return interact;
    }
    
    public boolean placesBlock() {
        return placeBlock != null;
    }

    public Material getPlaceBlock() {
        return placeBlock;
    }

    
    // -----------------
    
    private interface Property {
        void apply(ItemProperties prop);
    }
    
    private static Property tool() {
        return new Property() { public void apply(ItemProperties p) {
            p.tool = true;
        }};
    }

    private static Property food(final int heals) {
        return new Property() { public void apply(ItemProperties p) {
            p.heals = heals;
        }};
    }


    
    private static Property interact() {
        return new Property() { public void apply(ItemProperties p) {
            p.interact = true;
        }};
    }

    private static Property place(final Material mat) {
        return new Property() { public void apply(ItemProperties p) {
            p.placeBlock = mat;
        }};
    }
    
}
