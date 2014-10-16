package net.glowstone.generator;

import java.util.Random;

import net.glowstone.GlowWorld;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class HugeMushroomGenerator extends TreeGenericGenerator {
    private Material type;

    public HugeMushroomGenerator(Material type) {
        this(type, new BlockStateDelegate());
    }

    public HugeMushroomGenerator(Material type, BlockStateDelegate delegate) {
        super(delegate);
        if (!type.equals(Material.HUGE_MUSHROOM_1) && !type.equals(Material.HUGE_MUSHROOM_2)) {
            throw new IllegalArgumentException("Invalid huge mushroom type");
        }
        this.type = type;
    }

    @Override
    public boolean generate(Random random, Location loc) {
        // random height
        final int height = random.nextInt(3) + 4;
        final int sourceX = loc.getBlockX();
        final int sourceY = loc.getBlockY();
        final int sourceZ = loc.getBlockZ();

        // check height range
        if (sourceY < 1 || sourceY + height + 1 > 255) {
            return false;
        }

        final World world = (GlowWorld) loc.getWorld();
        //final Block block  = world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        // check below block
        Block b = world.getBlockAt(sourceX, sourceY - 1, sourceZ);
        if (!b.getType().equals(Material.DIRT) && !b.getType().equals(Material.GRASS)
                && !b.getType().equals(Material.MYCEL)) {
            return false;
        }

        // check around if there's enough space
        int x, y, z, radius;
        for (y = sourceY; y <= sourceY + 1 + height; y++) {
            // Space requirement is 7x7 blocks, so brown mushroom's cap
            // can be directly touching a mushroom next to it.
            // Since red mushrooms fits in 5x5 blocks it will never
            // touch another huge mushroom.
            radius = 3;
            if (y <= sourceY + 3) {
                radius = 0; // radius is 0 below 4 blocks tall (only the stem to take in account)
            }
            // check for block collision on horizontal slices
            for (x = sourceX - radius; x <= sourceX + radius; x++) {
                for (z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (y >= 0 && y < 256) {
                        // skip source block check
                        if (y != sourceY || x != sourceX || z != sourceZ) {
                            b = world.getBlockAt(x, y, z);
                            // we can overlap leaves around
                            if (!(b.getType().equals(Material.AIR)
                                    || b.getType().equals(Material.LEAVES)
                                    || b.getType().equals(Material.LEAVES_2))) {
                                return false;
                            }
                        }
                    } else { // height out of range
                        return false;
                    }
                }
            }
        }

        // generate the stem
        for (y = 0; y < height; y++) {
            delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, type, 10); // stem texture
        }

        // get the mushroom's cap Y start
        int capY = sourceY + height; // for brown mushroom it starts on top directly
        if (type.equals(Material.HUGE_MUSHROOM_2)) {
            capY = sourceY + height - 3; // for red mushroom, cap's thickness is 4 blocks
        }

        // generate mushroom's cap
        for (y = capY; y <= sourceY + height; y++) { // from bottom to top of mushroom
            radius = 1; // radius for the top of red mushroom
            if (y < sourceY + height) {
                radius = 2; // radius for red mushroom cap is 2
            }
            if (type.equals(Material.HUGE_MUSHROOM_1)) {
                radius = 3; // radius always 3 for a brown mushroom
            }
            // loop over horizontal slice
            for (x = sourceX - radius; x <= sourceX + radius; x++) {
                for (z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    int data = 5; // cap texture on top
                    // cap's borders/corners treatment
                    if (x == sourceX - radius) {
                        data = 4; // cap texture on top and west
                    } else if (x == sourceX + radius) {
                        data = 6; // cap texture on top and east
                    }
                    if (z == sourceZ - radius) {
                        data -= 3;
                    } else if (z == sourceZ + radius) {
                        data += 3;
                    }

                    // corners shrink treatment
                    // if it's a brown mushroom we need it always
                    // it's a red mushroom, it's only applied below the top
                    if (type.equals(Material.HUGE_MUSHROOM_1) || y < sourceY + height) {

                        // excludes the real corners of the cap structure
                        if ((x == sourceX - radius || x == sourceX + radius)
                                && (z == sourceZ - radius || z == sourceZ + radius)) {
                            continue;
                        }

                        // mushroom's cap corners treatment
                        if (x == sourceX - (radius - 1) && z == sourceZ - radius) {
                            data = 1; // cap texture on top, west and north
                        } else if (x == sourceX - radius && z == sourceZ - (radius - 1)) {
                            data = 1; // cap texture on top, west and north
                        } else if (x == sourceX + (radius - 1) && z == sourceZ - radius) {
                            data = 3; // cap texture on top, north and east
                        } else if (x == sourceX + radius && z == sourceZ - (radius - 1)) {
                            data = 3; // cap texture on top, north and east
                        } else if (x == sourceX - (radius - 1) && z == sourceZ + radius) {
                            data = 7; // cap texture on top, south and west
                        } else if (x == sourceX - radius && z == sourceZ + (radius - 1)) {
                            data = 7; // cap texture on top, south and west
                        } else if (x == sourceX + (radius - 1) && z == sourceZ + radius) {
                            data = 9; // cap texture on top, east and south
                        } else if (x == sourceX + radius && z == sourceZ + (radius - 1)) {
                            data = 9; // cap texture on top, east and south
                        }
                    }

                    // a data of 5 below the top layer means air
                    if (data != 5 || y >= sourceY + height) {
                        delegate.setTypeAndRawData(world, x, y, z, type, data);
                    }
                }
            }
        }

        return true;
    }
}
