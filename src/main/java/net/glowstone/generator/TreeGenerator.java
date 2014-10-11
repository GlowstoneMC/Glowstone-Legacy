package net.glowstone.generator;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.World;

public class TreeGenerator {
    private BlockStateDelegate delegate;
    private boolean forceUpdate;

    public TreeGenerator() {
        this(new BlockStateDelegate());
        forceUpdate = true;
    }

    public TreeGenerator(BlockStateDelegate delegate) {
        this.delegate = delegate;
        forceUpdate = false;
    }

    public boolean generate(Random random, Location loc, TreeType type) {
        // simple tree generation, always the same shape
        // determine species variant
        TreeSpecies species = getSpecies(type);
        Material wood = Material.LOG;
        Material leaves = Material.LEAVES;
        final int data = species.ordinal();
        if (species.ordinal() >= 4) {
            wood = Material.LOG_2;
            leaves = Material.LEAVES_2;
        }

        final World world = loc.getWorld();

        final int height = 4 + random.nextInt(3);
        // -1 here is a hack for implicit +1 in rest of code
        final int centerX = loc.getBlockX(), centerY = loc.getBlockY() - 1, centerZ = loc.getBlockZ();

        // top leaf layer
        delegate.setTypeAndRawData(world, centerX, centerY + height + 1, centerZ, leaves, data);
        for (int j = 0; j < 4; j++) {
            delegate.setTypeAndRawData(world, centerX, centerY + height + 1 - j, centerZ - 1, leaves, data);
            delegate.setTypeAndRawData(world, centerX, centerY + height + 1 - j, centerZ + 1, leaves, data);
            delegate.setTypeAndRawData(world, centerX - 1, centerY + height + 1 - j, centerZ, leaves, data);
            delegate.setTypeAndRawData(world, centerX + 1, centerY + height + 1 - j, centerZ, leaves, data);
        }

        // layer below top
        if (random.nextBoolean()) {
            delegate.setTypeAndRawData(world, centerX + 1, centerY + height, centerZ + 1, leaves, data);
        }
        if (random.nextBoolean()) {
            delegate.setTypeAndRawData(world, centerX + 1, centerY + height, centerZ - 1, leaves, data);
        }
        if (random.nextBoolean()) {
            delegate.setTypeAndRawData(world, centerX - 1, centerY + height, centerZ + 1, leaves, data);
        }
        if (random.nextBoolean()) {
            delegate.setTypeAndRawData(world, centerX - 1, centerY + height, centerZ - 1, leaves, data);
        }

        // two layers below that
        delegate.setTypeAndRawData(world, centerX + 1, centerY + height - 1, centerZ + 1, leaves, data);
        delegate.setTypeAndRawData(world, centerX + 1, centerY + height - 1, centerZ - 1, leaves, data);
        delegate.setTypeAndRawData(world, centerX - 1, centerY + height - 1, centerZ + 1, leaves, data);
        delegate.setTypeAndRawData(world, centerX - 1, centerY + height - 1, centerZ - 1, leaves, data);
        delegate.setTypeAndRawData(world, centerX + 1, centerY + height - 2, centerZ + 1, leaves, data);
        delegate.setTypeAndRawData(world, centerX + 1, centerY + height - 2, centerZ - 1, leaves, data);
        delegate.setTypeAndRawData(world, centerX - 1, centerY + height - 2, centerZ + 1, leaves, data);
        delegate.setTypeAndRawData(world, centerX - 1, centerY + height - 2, centerZ - 1, leaves, data);

        // outer leaves
        for (int j = 0; j < 2; j++) {
            for (int k = -2; k <= 2; k++) {
                for (int l = -2; l <= 2; l++) {
                    delegate.setTypeAndRawData(world, centerX + k, centerY + height - 1 - j, centerZ + l, leaves, data);
                }
            }
        }

        Material air = Material.AIR;
        for (int j = 0; j < 2; j++) {
            if (random.nextBoolean()) {
                delegate.setType(world, centerX + 2, centerY + height - 1 - j, centerZ + 2, air);
            }
            if (random.nextBoolean()) {
                delegate.setType(world, centerX + 2, centerY + height - 1 - j, centerZ - 2, air);
            }
            if (random.nextBoolean()) {
                delegate.setType(world, centerX - 2, centerY + height - 1 - j, centerZ + 2, air);
            }
            if (random.nextBoolean()) {
                delegate.setType(world, centerX - 2, centerY + height - 1 - j, centerZ - 2, air);
            }
        }

        // Trunk
        for (int y = 1; y <= height; y++) {
            delegate.setTypeAndRawData(world, centerX, centerY + y, centerZ, wood, data);
        }

        if (forceUpdate) {
            delegate.updateBlockStates();
        }

        return true;
    }

    private TreeSpecies getSpecies(TreeType type) {
        switch (type) {
            case TREE:
            case BIG_TREE:
            case SWAMP:
                return TreeSpecies.GENERIC;
            case REDWOOD:
            case TALL_REDWOOD:
            case MEGA_REDWOOD:
                return TreeSpecies.REDWOOD;
            case BIRCH:
            case TALL_BIRCH:
                return TreeSpecies.BIRCH;
            case JUNGLE:
            case SMALL_JUNGLE:
            case COCOA_TREE:
            case JUNGLE_BUSH:
                return TreeSpecies.JUNGLE;
            case ACACIA:
                return TreeSpecies.ACACIA;
            case DARK_OAK:
                return TreeSpecies.DARK_OAK;
            // unhandled
            case RED_MUSHROOM:
            case BROWN_MUSHROOM:
            default:
                return TreeSpecies.GENERIC;
        }
    }
}
