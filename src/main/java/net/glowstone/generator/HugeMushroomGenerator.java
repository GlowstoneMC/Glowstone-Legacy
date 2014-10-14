package net.glowstone.generator;

import java.util.Random;

import net.glowstone.GlowWorld;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class HugeMushroomGenerator extends TreeGenerator {
    private BlockStateDelegate delegate;
    private boolean forceUpdate;

    public HugeMushroomGenerator() {
        this(new BlockStateDelegate());
        forceUpdate = true;
    }

    public HugeMushroomGenerator(BlockStateDelegate delegate) {
        this.delegate = delegate;
        forceUpdate = false;
    }

    @Override
    public boolean generate(Random random, Location loc, TreeType treeType) {
        Material type;
        if (treeType.equals(TreeType.BROWN_MUSHROOM)) {
            type = Material.HUGE_MUSHROOM_1;
        } else if (treeType.equals(TreeType.RED_MUSHROOM)) {
            type = Material.HUGE_MUSHROOM_2;
        } else {
            return false;
        }

        // random height
        int height = random.nextInt(3) + 4;

        final World world = (GlowWorld) loc.getWorld();
        final Block block  = world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        // check height range
        if (block.getY() < 1 || block.getY() + height + 1 > 255) {
            return false;
        }

        // check below block
        Block b = block.getRelative(BlockFace.DOWN);
        if (!b.getType().equals(Material.DIRT) && !b.getType().equals(Material.GRASS)
                && !b.getType().equals(Material.MYCEL)) {
            return false;
        }

        boolean canGrow = true;
        int x, y, z, radius;
        // check around if there's enough space
        for (y = block.getY(); y <= block.getY() + 1 + height; y++) {
            // Space requirement is 7x7 blocks, so brown mushroom's cap
            // can be directly touching a mushroom next to it.
            // Since red mushrooms fits in 5x5 blocks it will never
            // touch another huge mushroom.
            radius = 3;
            if (y <= block.getY() + 3) {
                radius = 0; // radius is 0 below 4 blocks tall (only the stem to take in account)
            }
            // check for block collision on horizontal slices
            for (x = block.getX() - radius; x <= block.getX() + radius && canGrow; x++) {
                for (z = block.getZ() - radius; z <= block.getZ() + radius && canGrow; z++) {
                    if (y >= 0 && y < 256) {
                        // skip source block check
                        if (y != block.getY() || x != block.getX() || z != block.getZ()) {
                            b = world.getBlockAt(x, y, z);
                            // we can overlap leaves around
                            if (!(b.getType().equals(Material.AIR)
                                    || b.getType().equals(Material.LEAVES)
                                    || b.getType().equals(Material.LEAVES_2))) {
                                canGrow = false;
                            }
                        }
                    } else { // height out of range
                        canGrow = false;
                    }
                }
            }
        }
        if (!canGrow) {
            return false;
        }

        // generate the stem
        for (y = 0; y < height; y++) {
            delegate.setTypeAndRawData(world, block.getX(), block.getY() + y, block.getZ(), type, 10); // stem texture
        }

        // get the mushroom's cap Y start
        int capY = block.getY() + height; // for brown mushroom it starts on top directly
        if (type.equals(Material.HUGE_MUSHROOM_2)) {
            capY = block.getY() + height - 3; // for red mushroom, cap's thickness is 4 blocks
        }

        // generate mushroom's cap
        for (y = capY; y <= block.getY() + height; y++) { // from bottom to top of mushroom
            radius = 1; // radius for the top of red mushroom
            if (y < block.getY() + height) {
                radius = 2; // radius for red mushroom cap is 2
            }
            if (type.equals(Material.HUGE_MUSHROOM_1)) {
                radius = 3; // radius always 3 for a brown mushroom
            }
            // loop over horizontal slice
            for (x = block.getX() - radius; x <= block.getX() + radius; x++) {
                for (z = block.getZ() - radius; z <= block.getZ() + radius; z++) {
                    int data = 5; // cap texture on top
                    // cap's borders/corners treatment
                    if (x == block.getX() - radius) {
                        data = 4; // cap texture on top and west
                    } else if (x == block.getX() + radius) {
                        data = 6; // cap texture on top and east
                    }
                    if (z == block.getZ() - radius) {
                        data -= 3;
                    } else if (z == block.getZ() + radius) {
                        data += 3;
                    }

                    // corners shrink treatment
                    // if it's a brown mushroom we need it always
                    // it's a red mushroom, it's only applied below the top
                    if (type.equals(Material.HUGE_MUSHROOM_1) || y < block.getY() + height) {

                        // excludes the real corners of the cap structure
                        if ((x == block.getX() - radius || x == block.getX() + radius)
                                && (z == block.getZ() - radius || z == block.getZ() + radius)) {
                            continue;
                        }

                        // mushroom's cap corners treatment
                        if (x == block.getX() - (radius - 1) && z == block.getZ() - radius) {
                            data = 1; // cap texture on top, west and north
                        } else if (x == block.getX() - radius && z == block.getZ() - (radius - 1)) {
                            data = 1; // cap texture on top, west and north
                        } else if (x == block.getX() + (radius - 1) && z == block.getZ() - radius) {
                            data = 3; // cap texture on top, north and east
                        } else if (x == block.getX() + radius && z == block.getZ() - (radius - 1)) {
                            data = 3; // cap texture on top, north and east
                        } else if (x == block.getX() - (radius - 1) && z == block.getZ() + radius) {
                            data = 7; // cap texture on top, south and west
                        } else if (x == block.getX() - radius && z == block.getZ() + (radius - 1)) {
                            data = 7; // cap texture on top, south and west
                        } else if (x == block.getX() + (radius - 1) && z == block.getZ() + radius) {
                            data = 9; // cap texture on top, east and south
                        } else if (x == block.getX() + radius && z == block.getZ() + (radius - 1)) {
                            data = 9; // cap texture on top, east and south
                        }
                    }

                    // a data of 5 below the top layer means air
                    if (data != 5 || y >= block.getY() + height) {
                        delegate.setTypeAndRawData(world, x, y, z, type, data);
                    }
                }
            }
        }

        if (forceUpdate) {
            delegate.updateBlockStates();
        }

        return true;
    }
}