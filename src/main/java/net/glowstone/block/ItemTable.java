package net.glowstone.block;

import net.glowstone.block.blocktype.*;
import net.glowstone.block.itemtype.ItemPlaceAs;
import net.glowstone.block.itemtype.ItemSign;
import net.glowstone.block.itemtype.ItemType;
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

    private final Map<Integer, ItemType> idToType = new HashMap<>();

    private int nextBlockId, nextItemId;

    ////////////////////////////////////////////////////////////////////////////
    // Registration

    private void registerBuiltins() {
        reg(Material.NOTE_BLOCK, new BlockNote());
        reg(Material.MOB_SPAWNER, new BlockMobSpawner());
        reg(Material.SIGN_POST, new BlockSign());
        reg(Material.WALL_SIGN, new BlockSign());
        reg(Material.WORKBENCH, new BlockWorkbench());
        reg(Material.ENDER_CHEST, new BlockEnderchest());
        reg(Material.CHEST, new BlockChest());
        reg(Material.BOOKSHELF, new BlockBookshelf());
        reg(Material.CLAY, new BlockClay());
        reg(Material.DOUBLE_STEP, new BlockDoubleSlab());
        reg(Material.SOIL, new BlockFarmland());
        reg(Material.GLASS, new BlockGlass());
        reg(Material.THIN_GLASS, new BlockGlassPane());
        reg(Material.GLOWSTONE, new BlockGlowstone());
        reg(Material.MYCEL, new BlockMycelium());
        reg(Material.GRASS, new BlockGrass());
        reg(Material.DIRT, new BlockDirt());
        reg(Material.GRAVEL, new BlockGravel());
        reg(Material.ICE, new BlockIce());
        reg(Material.PACKED_ICE, new BlockIcePacked());
        reg(Material.SNOW, new BlockSnow());
        reg(Material.SNOW_BLOCK, new BlockSnowBlock());
        reg(Material.STONE, new BlockStone());
        reg(Material.COAL_ORE, new BlockCoalOre());
        reg(Material.DIAMOND_ORE, new BlockDiamondOre());
        reg(Material.EMERALD_ORE, new BlockEmeraldOre());
        reg(Material.LAPIS_ORE, new BlockLapisLazuliOre());
        reg(Material.QUARTZ_ORE, new BlockQuartzOre());
        reg(Material.REDSTONE_ORE, new BlockRedstoneOre());
        reg(Material.CARROT, new BlockCarrot());
        reg(Material.COCOA, new BlockCocoaPod());
        reg(Material.DEAD_BUSH, new BlockDeadBush());
        reg(Material.LONG_GRASS, new BlockTallGrass());
        reg(Material.HUGE_MUSHROOM_1, new BlockHugeMushroomBrown());
        reg(Material.HUGE_MUSHROOM_2, new BlockHugeMushroomRed());
        reg(Material.LEAVES, new BlockLeaves());
        reg(Material.LEAVES, new BlockLeaves());
        reg(Material.MELON_BLOCK, new BlockMelon());
        reg(Material.MELON_STEM, new BlockMelonStem());
        reg(Material.NETHER_WARTS, new BlockNetherWart());
        reg(Material.POTATO, new BlockPotato());
        reg(Material.PUMPKIN_STEM, new BlockPumpkinStem());
        reg(Material.CROPS, new BlockWheat());
        reg(Material.CAKE_BLOCK, new BlockCake());
        reg(Material.WEB, new BlockCobweb());
        reg(Material.FIRE, new BlockFire());
        reg(Material.MONSTER_EGGS, new BlockSilverfishEgg());

        reg(Material.SIGN, new ItemSign());
        reg(Material.REDSTONE, new ItemPlaceAs(Material.REDSTONE_WIRE));
        reg(Material.SUGAR_CANE, new ItemPlaceAs(Material.SUGAR_CANE_BLOCK));
        reg(Material.DIODE, new ItemPlaceAs(Material.DIODE_BLOCK_OFF));
        reg(Material.BREWING_STAND_ITEM, new ItemPlaceAs(Material.BREWING_STAND));
        reg(Material.CAULDRON_ITEM, new ItemPlaceAs(Material.CAULDRON));
        reg(Material.FLOWER_POT_ITEM, new ItemPlaceAs(Material.FLOWER_POT));
        reg(Material.SKULL_ITEM, new ItemPlaceAs(Material.SKULL));
        reg(Material.REDSTONE_COMPARATOR, new ItemPlaceAs(Material.REDSTONE_COMPARATOR_OFF));
    }

    private void reg(Material material, ItemType type) {
        if (material.isBlock() != (type instanceof BlockType)) {
            throw new IllegalArgumentException("Cannot mismatch item and block: " + material + ", " + type);
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
     *
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
