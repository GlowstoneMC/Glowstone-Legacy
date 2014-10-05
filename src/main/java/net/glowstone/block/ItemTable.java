package net.glowstone.block;

import net.glowstone.block.blocktype.*;
import net.glowstone.block.itemtype.*;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * The lookup table for block and item types.
 */
public final class ItemTable {

    private static final ItemTable INSTANCE = new ItemTable();

    static {
        INSTANCE.registerBuiltins();
    }

    public static ItemTable instance() {
        return INSTANCE;
    }

    private ItemTable() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Data

    private final Map<Integer, ItemType> idToType = new HashMap<>(512);

    private int nextBlockId, nextItemId;

    ////////////////////////////////////////////////////////////////////////////
    // Registration

    private void registerBuiltins() {
        reg(Material.BEDROCK, new BlockType().setHardness(-1.0D));
        reg(Material.BARRIER, new BlockType().setHardness(-1.0D));
        reg(Material.COMMAND, new BlockType().setHardness(-1.0D));
        reg(Material.ENDER_PORTAL_FRAME, new BlockEnderPortalFrame().setHardness(-1.0D));
        reg(Material.ENDER_PORTAL, new BlockType().setHardness(-1.0D));
        reg(Material.PORTAL, new BlockType().setHardness(-1.0D));
        reg(Material.NOTE_BLOCK, new BlockNote().setHardness(0.8D));
        reg(Material.MOB_SPAWNER, new BlockMobSpawner().setHardness(5.0D));
        reg(Material.SIGN_POST, new BlockSign().setHardness(1.0D));
        reg(Material.WALL_SIGN, new BlockSign().setHardness(1.0D));
        reg(Material.WORKBENCH, new BlockWorkbench().setHardness(2.5D));
        reg(Material.ENDER_CHEST, new BlockEnderchest().setHardness(22.5D));
        reg(Material.CHEST, new BlockChest().setHardness(2.5D));
        reg(Material.DISPENSER, new BlockDispenser().setHardness(3.5D));
        reg(Material.DROPPER, new BlockDropper().setHardness(3.5D));
        reg(Material.BOOKSHELF, new BlockDirectDrops(Material.BOOK, 3).setHardness(1.5D));
        reg(Material.CLAY, new BlockDirectDrops(Material.CLAY_BALL, 4).setHardness(0.6D));
        reg(Material.DOUBLE_STEP, new BlockDoubleSlab().setHardness(2.0D));
        reg(Material.SOIL, new BlockDirectDrops(Material.DIRT).setHardness(0.6D));
        reg(Material.GLASS, new BlockDropless().setHardness(0.3D));
        reg(Material.THIN_GLASS, new BlockDropless().setHardness(0.3D));
        reg(Material.GLOWSTONE, new BlockRandomDrops(Material.GLOWSTONE_DUST, 2, 4).setHardness(0.3D));
        reg(Material.MYCEL, new BlockDirectDrops(Material.DIRT).setHardness(0.6D));
        reg(Material.GRASS, new BlockDirectDrops(Material.DIRT).setHardness(0.6D));
        reg(Material.DIRT, new BlockDirectDrops(Material.DIRT).setHardness(0.5D));
        reg(Material.GRAVEL, new BlockGravel().setHardness(0.6D));
        reg(Material.ICE, new BlockDropless().setHardness(0.5D));
        reg(Material.PACKED_ICE, new BlockDropless().setHardness(0.5D));
        reg(Material.SNOW, new BlockSnow().setHardness(0.1D));
        reg(Material.SNOW_BLOCK, new BlockDropless().setHardness(0.2D));
        reg(Material.STONE, new BlockDirectDrops(Material.COBBLESTONE).setHardness(1.5D));
        reg(Material.COAL_ORE, new BlockDirectDrops(Material.COAL).setHardness(3.0D));
        reg(Material.DIAMOND_ORE, new BlockDirectDrops(Material.DIAMOND).setHardness(3.0D));
        reg(Material.EMERALD_ORE, new BlockDirectDrops(Material.EMERALD).setHardness(3.0D));
        reg(Material.LAPIS_ORE, new BlockRandomDrops(Material.INK_SACK, 4, 4, 8).setHardness(3.0D));
        reg(Material.QUARTZ_ORE, new BlockDirectDrops(Material.QUARTZ).setHardness(3.0D));
        reg(Material.REDSTONE_ORE, new BlockRandomDrops(Material.REDSTONE, 0, 3, 4).setHardness(3.0D));
        reg(Material.CARROT, new BlockDirectDrops(Material.CARROT_ITEM).setHardness(0.0D));
        reg(Material.COCOA, new BlockDirectDrops(Material.INK_SACK, 3, 1).setHardness(0.2D));
        reg(Material.DEAD_BUSH, new BlockDropless().setHardness(0.0D));
        reg(Material.LONG_GRASS, new BlockTallGrass().setHardness(0.0D));
        reg(Material.HUGE_MUSHROOM_1, new BlockHugeMushroom(true).setHardness(0.2D));
        reg(Material.HUGE_MUSHROOM_2, new BlockHugeMushroom(false).setHardness(0.2D));
        reg(Material.LEAVES, new BlockLeaves().setHardness(0.2D));
        reg(Material.LEAVES_2, new BlockLeaves().setHardness(0.2D));
        reg(Material.MELON_BLOCK, new BlockMelon().setHardness(1.0D));
        reg(Material.MELON_STEM, new BlockMelonStem().setHardness(0.0D));
        reg(Material.NETHER_WARTS, new BlockDirectDrops(Material.NETHER_STALK).setHardness(0.0D));
        reg(Material.POTATO, new BlockDirectDrops(Material.POTATO_ITEM).setHardness(0.0D));
        reg(Material.PUMPKIN_STEM, new BlockPumpkinStem().setHardness(0.0D));
        reg(Material.CROPS, new BlockDirectDrops(Material.SEEDS).setHardness(0.0D));
        reg(Material.CAKE_BLOCK, new BlockDropless().setHardness(0.5D));
        reg(Material.WEB, new BlockDirectDrops(Material.STRING).setHardness(4.0D));
        reg(Material.FIRE, new BlockFire().setHardness(0.0D));
        reg(Material.MONSTER_EGGS, new BlockDropless().setHardness(0.75D));
        reg(Material.FURNACE, new BlockFurnace().setHardness(3.5D));
        reg(Material.LEVER, new BlockLever().setHardness(0.5D));
        reg(Material.HOPPER, new BlockHopper().setHardness(3.0D));
        reg(Material.ACACIA_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.BIRCH_WOOD_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.BRICK_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.COBBLESTONE_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.DARK_OAK_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.JUNGLE_WOOD_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.NETHER_BRICK_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.QUARTZ_STAIRS, new BlockStairs().setHardness(0.8D));
        reg(Material.SANDSTONE_STAIRS, new BlockStairs().setHardness(0.8D));
        reg(Material.SPRUCE_WOOD_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.SMOOTH_STAIRS, new BlockStairs().setHardness(1.5D));
        reg(Material.WOOD_STAIRS, new BlockStairs().setHardness(2.0D));
        reg(Material.STEP, new BlockSlab().setHardness(2.0D));
        reg(Material.WOOD_STEP, new BlockSlab().setHardness(2.0D));
        reg(Material.HAY_BLOCK, new BlockHay().setHardness(0.5D));
        reg(Material.QUARTZ_BLOCK, new BlockQuartz().setHardness(0.8D));
        reg(Material.LOG, new BlockLog().setHardness(2.0D));
        reg(Material.LOG_2, new BlockLog2().setHardness(2.0D));
        reg(Material.LADDER, new BlockLadder().setHardness(0.4D));
        reg(Material.VINE, new BlockVine().setHardness(0.2D));
        reg(Material.STONE_BUTTON, new BlockButton(Material.STONE_BUTTON).setHardness(0.5D));
        reg(Material.WOOD_BUTTON, new BlockButton(Material.WOOD_BUTTON).setHardness(0.5D));
        reg(Material.BED_BLOCK, new BlockBed().setHardness(0.2D));
        reg(Material.TORCH, new BlockTorch().setHardness(0.0D));
        reg(Material.DAYLIGHT_DETECTOR, new BlockDaylightDetector().setHardness(0.2D));
        reg(Material.DAYLIGHT_DETECTOR_INVERTED, new BlockDaylightDetector().setHardness(0.2D));
        reg(Material.ENCHANTMENT_TABLE, new BlockEnchantmentTable().setHardness(5.0D));
        reg(Material.ANVIL, new BlockAnvil().setHardness(5.0D));
        reg(Material.BREWING_STAND, new BlockBrewingStand().setHardness(0.5D));
        reg(Material.WATER, new BlockWater().setHardness(100D));
        reg(Material.STATIONARY_WATER, new BlockWater().setHardness(100D));
        reg(Material.LAVA, new BlockLava().setHardness(100D));
        reg(Material.STATIONARY_LAVA, new BlockLava().setHardness(100D));

        reg(Material.SIGN, new ItemSign());
        reg(Material.REDSTONE, new ItemPlaceAs(Material.REDSTONE_WIRE));
        reg(Material.SUGAR_CANE, new ItemPlaceAs(Material.SUGAR_CANE_BLOCK));
        reg(Material.DIODE, new ItemPlaceAs(Material.DIODE_BLOCK_OFF));
        reg(Material.BREWING_STAND_ITEM, new ItemPlaceAs(Material.BREWING_STAND));
        reg(Material.CAULDRON_ITEM, new ItemPlaceAs(Material.CAULDRON));
        reg(Material.FLOWER_POT_ITEM, new ItemPlaceAs(Material.FLOWER_POT));
        reg(Material.SKULL_ITEM, new ItemPlaceAs(Material.SKULL));
        reg(Material.REDSTONE_COMPARATOR, new ItemPlaceAs(Material.REDSTONE_COMPARATOR_OFF));
        reg(Material.BED, new ItemPlaceAs(Material.BED_BLOCK));
        reg(Material.BUCKET, new ItemBucket());
        reg(Material.WATER_BUCKET, new ItemFilledBucket(Material.WATER));
        reg(Material.LAVA_BUCKET, new ItemFilledBucket(Material.LAVA));
        reg(Material.WOOD_HOE, new ItemHoe());
        reg(Material.STONE_HOE, new ItemHoe());
        reg(Material.IRON_HOE, new ItemHoe());
        reg(Material.GOLD_HOE, new ItemHoe());
        reg(Material.DIAMOND_HOE, new ItemHoe());
        reg(Material.SEEDS, new ItemSeeds(Material.CROPS, Material.SOIL));
        reg(Material.MELON_SEEDS, new ItemSeeds(Material.MELON_STEM, Material.SOIL));
        reg(Material.PUMPKIN_SEEDS, new ItemSeeds(Material.PUMPKIN_STEM, Material.SOIL));
        reg(Material.NETHER_STALK, new ItemSeeds(Material.NETHER_WARTS, Material.SOUL_SAND));
        reg(Material.CARROT_ITEM, new ItemFoodSeeds(Material.CARROT, Material.SOIL));
        reg(Material.POTATO_ITEM, new ItemFoodSeeds(Material.POTATO, Material.SOIL));
        reg(Material.INK_SACK, new ItemDye());
    }

    private void reg(Material material, ItemType type) {
        if (material.isBlock() != (type instanceof BlockType)) {
            throw new IllegalArgumentException("Cannot mismatch item and block: " + material + ", " + type);
        }

        if (idToType.containsKey(material.getId())) {
            throw new IllegalArgumentException("Cannot use " + type + " for " + material + ", is already " + idToType.get(material.getId()));
        }

        idToType.put(material.getId(), type);
        type.setId(material.getId());

        if (material.isBlock()) {
            nextBlockId = Math.max(nextBlockId, material.getId() + 1);
        } else {
            nextItemId = Math.max(nextItemId, material.getId() + 1);
        }
    }

    /**
     * Register a new, non-Vanilla ItemType. It will be assigned an ID automatically.
     * @param type the ItemType to register.
     */
    public void register(ItemType type) {
        int id;
        if (type instanceof BlockType) {
            id = nextBlockId;
        } else {
            id = nextItemId;
        }

        while (idToType.containsKey(id)) {
            ++id;
        }

        idToType.put(id, type);
        type.setId(id);

        if (type instanceof BlockType) {
            nextBlockId = id + 1;
        } else {
            nextItemId = id + 1;
        }
    }

    private ItemType createDefault(int id) {
        Material material = Material.getMaterial(id);
        if (material == null || id == 0) {
            return null;
        }

        ItemType result;
        if (material.isBlock()) {
            result = new BlockType();
        } else {
            result = new ItemType();
        }
        reg(material, result);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Type access

    public ItemType getItem(int id) {
        ItemType type = idToType.get(id);
        if (type == null) {
            type = createDefault(id);
        }
        return type;
    }

    public BlockType getBlock(int id) {
        ItemType itemType = getItem(id);
        if (itemType instanceof BlockType) {
            return (BlockType) itemType;
        }
        return null;
    }

    public ItemType getItem(Material mat) {
        return getItem(mat.getId());
    }

    public BlockType getBlock(Material mat) {
        return getBlock(mat.getId());
    }

}
