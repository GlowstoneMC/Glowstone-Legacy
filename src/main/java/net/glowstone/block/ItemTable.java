package net.glowstone.block;

import net.glowstone.block.blocktype.*;
import net.glowstone.block.itemtype.ItemPlaceAs;
import net.glowstone.block.itemtype.ItemSign;
import net.glowstone.block.itemtype.ItemType;

import org.bukkit.Material;
import org.bukkit.Sound;

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

    private ItemTable() {}

    ////////////////////////////////////////////////////////////////////////////
    // Data

    private final Map<Integer, ItemType> idToType = new HashMap<>(512);

    private int nextBlockId, nextItemId;

    ////////////////////////////////////////////////////////////////////////////
    // Registration

    private void registerBuiltins() {
        reg(Material.NOTE_BLOCK, new BlockNote()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.MOB_SPAWNER, new BlockMobSpawner()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.SIGN_POST, new BlockSign());
        reg(Material.WALL_SIGN, new BlockSign());
        reg(Material.WORKBENCH, new BlockWorkbench());
        reg(Material.ENDER_CHEST, new BlockEnderchest()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.CHEST, new BlockChest());
        reg(Material.BOOKSHELF, new BlockDirectDrops(Material.BOOK, 3));
        reg(Material.CLAY, new BlockDirectDrops(Material.CLAY_BALL, 4)).setPlaceSound(Sound.DIG_GRAVEL);
        reg(Material.DOUBLE_STEP, new BlockDoubleSlab()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.SOIL, new BlockDirectDrops(Material.DIRT)).setPlaceSound(Sound.DIG_GRAVEL);
        reg(Material.GLASS, new BlockDropless()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.THIN_GLASS, new BlockDropless()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.GLOWSTONE, new BlockRandomDrops(Material.GLOWSTONE_DUST, 2, 4)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.MYCEL, new BlockDirectDrops(Material.DIRT)).setPlaceSound(Sound.DIG_GRAVEL);
        reg(Material.GRASS, new BlockDirectDrops(Material.DIRT)).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.DIRT, new BlockDirectDrops(Material.DIRT)).setPlaceSound(Sound.DIG_GRAVEL);
        reg(Material.GRAVEL, new BlockGravel()).setPlaceSound(Sound.DIG_GRAVEL);
        reg(Material.ICE, new BlockDropless()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.PACKED_ICE, new BlockDropless()).setPlaceSound(Sound.DIG_STONE);
        reg(Material.SNOW, new BlockDropless()).setPlaceSound(Sound.DIG_SNOW);
        reg(Material.SNOW_BLOCK, new BlockDropless()).setPlaceSound(Sound.DIG_SNOW);
        reg(Material.STONE, new BlockDirectDrops(Material.COBBLESTONE)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.COAL_ORE, new BlockDirectDrops(Material.COAL)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.DIAMOND_ORE, new BlockDirectDrops(Material.DIAMOND)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.EMERALD_ORE, new BlockDirectDrops(Material.EMERALD)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.LAPIS_ORE, new BlockRandomDrops(Material.INK_SACK, 4, 4, 8)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.QUARTZ_ORE, new BlockDirectDrops(Material.QUARTZ)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.REDSTONE_ORE, new BlockRandomDrops(Material.REDSTONE, 0, 3, 4)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.CARROT, new BlockDirectDrops(Material.CARROT_ITEM)).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.COCOA, new BlockDirectDrops(Material.INK_SACK, 3, 1));
        reg(Material.DEAD_BUSH, new BlockDropless()).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.LONG_GRASS, new BlockTallGrass()).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.HUGE_MUSHROOM_1, new BlockHugeMushroom(true));
        reg(Material.HUGE_MUSHROOM_2, new BlockHugeMushroom(false));
        reg(Material.LEAVES, new BlockLeaves()).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.LEAVES_2, new BlockLeaves()).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.MELON_BLOCK, new BlockMelon());
        reg(Material.MELON_STEM, new BlockMelonStem()).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.NETHER_WARTS, new BlockDirectDrops(Material.NETHER_STALK)).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.POTATO, new BlockDirectDrops(Material.POTATO_ITEM)).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.PUMPKIN_STEM, new BlockPumpkinStem()).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.CROPS, new BlockDirectDrops(Material.SEEDS)).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.CAKE_BLOCK, new BlockDropless()).setPlaceSound(Sound.DIG_WOOL);
        reg(Material.WEB, new BlockDirectDrops(Material.STRING)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.FIRE, new BlockDropless()).setPlaceSound(Sound.DIG_STONE); //Maybe
        reg(Material.MONSTER_EGGS, new BlockDropless()).setPlaceSound(Sound.DIG_STONE);

        reg(Material.SIGN, new ItemSign());
        reg(Material.REDSTONE, new ItemPlaceAs(Material.REDSTONE_WIRE)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.SUGAR_CANE, new ItemPlaceAs(Material.SUGAR_CANE_BLOCK)).setPlaceSound(Sound.DIG_GRASS);
        reg(Material.DIODE, new ItemPlaceAs(Material.DIODE_BLOCK_OFF)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.BREWING_STAND_ITEM, new ItemPlaceAs(Material.BREWING_STAND)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.CAULDRON_ITEM, new ItemPlaceAs(Material.CAULDRON)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.FLOWER_POT_ITEM, new ItemPlaceAs(Material.FLOWER_POT)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.SKULL_ITEM, new ItemPlaceAs(Material.SKULL)).setPlaceSound(Sound.DIG_STONE);
        reg(Material.REDSTONE_COMPARATOR, new ItemPlaceAs(Material.REDSTONE_COMPARATOR_OFF)).setPlaceSound(Sound.DIG_STONE);
        
        //BeYkeRYkt - Start
        reg(Material.SAND, new BlockDirectDrops(Material.SAND)).setPlaceSound(Sound.DIG_SAND);
        //BeYkeRYkt - End
    }

    private BlockType reg(Material material, ItemType type) {
        if (material.isBlock() != (type instanceof BlockType)) {
            throw new IllegalArgumentException("Cannot mismatch item and block: " + material + ", " + type);
        }
        idToType.put(material.getId(), type);
        type.setId(material.getId());

        if (material.isBlock()) {
            nextBlockId = Math.max(nextBlockId, material.getId() + 1);
            //BeYkeRYkt - Start
            BlockType block = (BlockType) type;
            return block;
            //BeYkeRYkt - End
        } else {
            nextItemId = Math.max(nextItemId, material.getId() + 1);
            //BeYkeRYkt - Start
            BlockType block = type.getPlaceAs();
            return block;
            //BeYkeRYkt - End
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
