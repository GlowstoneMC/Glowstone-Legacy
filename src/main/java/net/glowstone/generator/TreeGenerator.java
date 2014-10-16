package net.glowstone.generator;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;

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
        //TreeSpecies species = getSpecies(type);
        TreeGenericGenerator generator = null;
        switch (type) {
            case TREE:
            case BIG_TREE:
            case SWAMP:
            case REDWOOD:
            case TALL_REDWOOD:
            case MEGA_REDWOOD:
                generator = new TreeGenericGenerator(delegate);
                break;
            case BIRCH:
                generator = new TreeBirchGenerator(delegate);
                break;
            case TALL_BIRCH:
                generator = new TreeBirchGenerator(true, delegate);
                break;
            case JUNGLE:
            case SMALL_JUNGLE:
            case COCOA_TREE:
            case JUNGLE_BUSH:
            case ACACIA:
            case DARK_OAK:
                generator = new TreeGenericGenerator(delegate);
                break;
            // unhandled
            case RED_MUSHROOM:
                generator = new HugeMushroomGenerator(Material.HUGE_MUSHROOM_1, delegate);
                break;
            case BROWN_MUSHROOM:
                generator = new HugeMushroomGenerator(Material.HUGE_MUSHROOM_2, delegate);
                break;
            default:
                return false;
        }

        if (generator.generate(random, loc)) {

            if (forceUpdate) {
                delegate.updateBlockStates();
            }
            return true;
        }

        return false;
    }
}
