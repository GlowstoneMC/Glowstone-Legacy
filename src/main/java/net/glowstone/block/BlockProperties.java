package net.glowstone.block;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.event.Event.Result;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.glowstone.block.properties.*;

/**
 * An enum containing an entry for every block describing that block's physical properties.
 */ public enum BlockProperties {
    
    AIR(passthru()),
    STONE(drops(false, Material.COBBLESTONE)), //todo: tool(ToolType.PICKAXE, ToolLevel.WOOD)
    GRASS(drops(false, Material.DIRT)),
    DIRT(),
    COBBLESTONE(),
    WOOD(),
    SAPLING(passthru(), place(Material.GRASS, Material.DIRT, Material.SOIL)),
    BEDROCK(drops()),
    WATER(passthru(), physics(), opaque(2)),
    STATIONARY_WATER(passthru(), physics(), opaque(2)),
    LAVA(passthru(), physics(), emitsLight(15)),
    STATIONARY_LAVA(passthru(), physics(), emitsLight(15)),
    SAND(physics()),
    GRAVEL(physics(), drops(), specialClass(GravelProperties.class)),
    GOLD_ORE(),
    IRON_ORE(),
    COAL_ORE(drops(false, Material.COAL)),
    LOG(),
    LEAVES(physics(), drops(), interact(), opaque(1), specialClass(LeavesProperties.class)),
    SPONGE(),
    GLASS(drops()),
    LAPIS_ORE(drops(), specialClass(LapisOreProperties.class)),
    LAPIS_BLOCK(),
    DISPENSER(interact(), place(), redstone()),
    SANDSTONE(),
    NOTE_BLOCK(interact(), redstone()),
    BED_BLOCK(interact(), height(0.5625F)),
    POWERED_RAIL(place(), passthru(), physics(), redstone(), specialClass(RailProperties.class)),
    DETECTOR_RAIL(place(), passthru(), physics(), redstone(), specialClass(RailProperties.class)),
    PISTON_STICKY_BASE(place(), redstone()),
    WEB(passthru()),
    LONG_GRASS(passthru(), drops(), place(Material.GRASS)),
    DEAD_BUSH(passthru(), drops(), place(Material.SAND)),
    PISTON_BASE(place(), redstone()),
    PISTON_EXTENSION(redstone()),
    WOOL(drops(true, Material.WOOL)),
    PISTON_MOVING_PIECE(redstone()),
    YELLOW_FLOWER(place(Material.GRASS, Material.DIRT, Material.SOIL), passthru()),
    RED_ROSE(place(Material.GRASS, Material.DIRT, Material.SOIL), passthru()),
    BROWN_MUSHROOM(place(Material.GRASS, Material.DIRT, Material.SOIL), passthru()),
    RED_MUSHROOM(place(Material.GRASS, Material.DIRT, Material.SOIL), passthru()),
    GOLD_BLOCK(),
    IRON_BLOCK(),
    DOUBLE_STEP(drops(true, new ItemStack(Material.STEP, 2))),
    STEP(place(), passthru(), drops(true, Material.STEP), height(0.5F)), // todo: better custom place
    BRICK(),
    TNT(redstone(), hardness(0F)),
    BOOKSHELF(drops()),
    MOSSY_COBBLESTONE(),
    OBSIDIAN(),
    TORCH(place(), passthru(), emitsLight(14), specialClass(TorchProperties.class), hardness(0F)),
    FIRE(passthru(), emitsLight(15), drops(), hardness(0F)),
    MOB_SPAWNER(drops()),
    WOOD_STAIRS(place(), passthru()),
    CHEST(interact()),
    REDSTONE_WIRE(redstone(), drops(false, Material.REDSTONE), hardness(0F)),
    DIAMOND_ORE(drops(false, Material.DIAMOND)),
    DIAMOND_BLOCK(),
    WORKBENCH(interact()),
    CROPS(physics(), passthru()),
    SOIL(),
    FURNACE(interact(), place()),
    BURNING_FURNACE(interact(), place()),
    SIGN_POST(passthru(), place(), drops(false, Material.SIGN)),
    WOODEN_DOOR(passthru(), interact(), place(), drops(false, Material.WOOD_DOOR), specialClass(DoorProperties.class)),
    LADDER(place(), passthru()),
    RAILS(place(), passthru(), physics(), redstone(), specialClass(RailProperties.class)),
    COBBLESTONE_STAIRS(place(), passthru()),
    WALL_SIGN(passthru(), drops(false, Material.SIGN)),
    LEVER(place(), interact(), passthru(), redstone(), specialClass(LeverProperties.class)),
    STONE_PLATE(place(), passthru(), redstone()),
    IRON_DOOR_BLOCK(place(), passthru(), drops(false, Material.IRON_DOOR), specialClass(DoorProperties.class)),
    WOOD_PLATE(place(), passthru(), redstone()),
    REDSTONE_ORE(interact(), drops(), specialClass(RedstoneOreProperties.class)),
    GLOWING_REDSTONE_ORE(interact(), physics(), drops(false, Material.REDSTONE_ORE), specialClass(RedstoneOreProperties.class)),
    REDSTONE_TORCH_OFF(passthru(), redstone(), hardness(0F), drops(false, Material.REDSTONE_TORCH_ON)),
    REDSTONE_TORCH_ON(passthru(), redstone(), hardness(0F)),
    STONE_BUTTON(passthru(), interact(), redstone()),
    SNOW(passthru(), drops(false, Material.SNOW_BALL)),
    ICE(opaque(2), drops()),
    SNOW_BLOCK(drops(false, new ItemStack(Material.SNOW_BALL, 4))),
    CACTUS(place(Material.SAND), physics()),
    CLAY(drops(false, new ItemStack(Material.CLAY_BALL, 4))),
    SUGAR_CANE_BLOCK(place(Material.GRASS, Material.DIRT, Material.SOIL), drops(false, Material.SUGAR_CANE), passthru(), hardness(0F)),
    JUKEBOX(interact()),
    FENCE(place(), opaque(0), height(1.5F)),
    PUMPKIN(place()), //todo: exclusive place restrictions
    NETHERRACK(),
    SOUL_SAND(),
    GLOWSTONE(drops(false, new ItemStack(Material.GLOWSTONE_DUST, 4))),
    PORTAL(place(), physics()),
    JACK_O_LANTERN(place(), emitsLight(15)),
    CAKE_BLOCK(height(0.4375F)),
    DIODE_BLOCK_OFF(passthru(), redstone(), interact(), specialClass(DiodeProperties.class), drops(false, Material.DIODE), hardness(0F)),
    DIODE_BLOCK_ON(passthru(), redstone(), interact(), specialClass(DiodeProperties.class), drops(false, Material.DIODE), hardness(0F)),
    LOCKED_CHEST(emitsLight(15), hardness(0F)),
    TRAP_DOOR(redstone(), interact(), place(), passthru(), specialClass(TrapdoorProperties.class));
    
    // -----------------
    
    private static EnumMap<Material, BlockProperties> materialMap = new EnumMap<Material, BlockProperties>(Material.class);
    
    static {
        for (BlockProperties prop : values()) {
            materialMap.put(prop.material, prop);
        }
    }
    
    public static BlockProperties get(Material material) {
        return materialMap.get(material);
    }
    
    public static BlockProperties get(int id) {
        return materialMap.get(Material.getMaterial(id));
    }
    
    // -----------------
    
    private final Material material;
    private ItemStack[] drops;
    private ItemStack[] dataDrops;
    private boolean physics = false;
    private boolean redstone = true;
    private boolean interact = false;
    private boolean place = false;
    private boolean solid = true;
    private int emitsLight = 0;
    private int blocksLight = 15;
    private Material[] placeMaterials;
    private Class<?> specialClass;
    private float height = 1F;
    private float hardness = 1F;
    private Method dropMethod;
    private Method canPlaceMethod;
    private Method interactMethod;
    private Method doPlaceMethod;
    
    private BlockProperties(Property... props) {
        material = Material.getMaterial(toString());
        drops = new ItemStack[] { new ItemStack(material, 1) };
        
        for (Property p : props)
            p.apply(this);
        if (specialClass  != null) {
            try {
                dropMethod = specialClass.getMethod("drops");
            } catch (NoSuchMethodException e) {
                dropMethod = null;
            }
            try {
                canPlaceMethod = specialClass.getMethod("canPlace", Location.class);
            } catch (NoSuchMethodException e) {
                canPlaceMethod = null;
            }
            try {
                interactMethod = specialClass.getMethod("doInteract", PlayerInteractEvent.class);
            } catch (NoSuchMethodException e) {
                interactMethod = null;
            }
            try {
                doPlaceMethod = specialClass.getMethod("doPlace", Player.class, Block.class, BlockFace.class);
            } catch (NoSuchMethodException e) {
                doPlaceMethod = null;
            }
        }
    }
    
    public ItemStack[] getDrops() {
        if (dataDrops != null) {
            return dataDrops;
        } else if (specialClass != null) {
            try {
                return (ItemStack[])dropMethod.invoke(null);
            } catch (IllegalAccessException e) {
                return drops;
            } catch (InvocationTargetException e) {
                return drops;
            }
        }
        return drops;
    }

    public ItemStack[] getDropsWithData(short data) {
        if (dataDrops != null) {
            ItemStack[] ret = new ItemStack[dataDrops.length];
            for (int i = 0; i < drops.length; i++) {
                ret[i] = drops[i];
                ret[i].setDurability(data);
            }
            return ret;
        } else if (specialClass != null) {
            try {
                if (dropMethod == null)
                    return drops;
                return (ItemStack[])dropMethod.invoke(null);
            } catch (IllegalAccessException e) {
                return drops;
            } catch (InvocationTargetException e) {
                return drops;
            }
        }
        return drops;
    }
    
    public boolean hasPhysics() {
        return physics;
    }

    public float getHeight() {
        return height;
    }
    
    public boolean hasRedstone() {
        return redstone;
    }
    
    public boolean isInteractable() {
        return interact;
    }
    
    public boolean specialPlaceable() {
        return place;
    }

    public PlayerInteractEvent doInteract(PlayerInteractEvent event) {
        if (interactMethod != null && event.useInteractedBlock() != Result.DENY)
        try {
            return (PlayerInteractEvent)interactMethod.invoke(null, event);
        } catch (IllegalAccessException e) {
            event.setUseInteractedBlock(Result.DENY);
            return event;
        } catch (InvocationTargetException e) {
            event.setUseInteractedBlock(Result.DENY);
            return event;
        }
        event.setUseInteractedBlock(Result.DENY);
        return event;
    }

    public boolean isPlaceableAt(Location loc) {

        if (place == true && placeMaterials != null)
            return Arrays.asList(placeMaterials).contains(loc.getBlock().getRelative(BlockFace.DOWN).getType());
        if (place == false || canPlaceMethod == null)
            return true;
        try {
            return (Boolean)canPlaceMethod.invoke(null, loc);
        } catch (IllegalAccessException e) {
            return true;
        } catch (InvocationTargetException e) {
            return true;
        }
    }

    public Byte specialPlace(Player player, Block block, BlockFace against) {
        if (place == false || doPlaceMethod == null)
            return null;
        try {
            return (Byte)doPlaceMethod.invoke(null, player, block, against);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }
    
    public boolean isSolid() {
        return solid;
    }
    
    public int emittedLightLevel() {
        return emitsLight;
    }
    
    public int blockedLightLevel() {
        return blocksLight;
    }

    public float getHardness() {
        return hardness;
    }
    
    // -----------------
    
    private interface Property {
        void apply(BlockProperties prop);
    }
    
    private static Property drops(final boolean data, final ItemStack... mats) {
        return new Property() { public void apply(BlockProperties p) {
            if (data) {
                p.dataDrops = mats;
            } else {
                p.drops = mats;
            }
        }};
    }
    
    private static Property drops(final boolean  data, final Material mat) {
        return new Property() { public void apply(BlockProperties p) {
            if (data) {
                p.dataDrops = new ItemStack[] { new ItemStack(mat, 1) };
            }
            p.drops = new ItemStack[] { new ItemStack(mat, 1) };
        }};
    }

    private static Property drops() {
        return new Property() { public void apply(BlockProperties p) {
                p.drops = null;
                p.dataDrops = null;
        }};
    }
    
    private static Property passthru() {
        return new Property() { public void apply(BlockProperties p) {
            p.solid = false;
            p.blocksLight = 0;
        }};
    }
    
    private static Property opaque(final int level) {
        return new Property() { public void apply(BlockProperties p) {
            p.blocksLight = level;
        }};
    }
    
    private static Property emitsLight(final int level) {
        return new Property() { public void apply(BlockProperties p) {
            p.emitsLight = level;
        }};
    }
    
    private static Property physics() {
        return new Property() { public void apply(BlockProperties p) {
            p.physics = true;
        }};
    }
    
    private static Property redstone() {
        return new Property() { public void apply(BlockProperties p) {
            p.redstone = true;
        }};
    }

    private static Property hardness(final float hardness) {
        return new Property() { public void apply(BlockProperties p) {
            p.hardness = hardness;
        }};
    }
    
    private static Property interact() {
        return new Property() { public void apply(BlockProperties p) {
            p.interact = true;
        }};
    }
    
    private static Property place() {
        return new Property() { public void apply(BlockProperties p) {
            p.place = true;
        }};
    }

    private static Property place(final Material ... mats) {
        return new Property() { public void apply(BlockProperties p) {
            p.place = true;
            p.placeMaterials = mats;
        }};
    }

    private static Property specialClass(final Class<?> clazz) {
        return new Property() { public void apply(BlockProperties p) {
            p.specialClass = clazz;
        }};
    }

    private static Property height(final float height) {
        return new Property() { public void apply(BlockProperties p) {
                p.height = height;
        }};
    }
}
