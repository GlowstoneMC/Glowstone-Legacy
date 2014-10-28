package net.glowstone.generator;

import java.util.Random;

import net.glowstone.generator.objects.trees.*;
import net.glowstone.util.BlockStateDelegate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;

public class TreeGenerator {

    private final BlockStateDelegate delegate;
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
        GenericTree tree;
        switch (type) {
            case TREE:
            case BIG_TREE:
                tree = new GenericTree(random, delegate);
                break;
            case SWAMP:
                tree = new SwampTree(random, delegate);
                break;
            case REDWOOD:
                tree = new RedwoodTree(random, delegate);
                break;
            case TALL_REDWOOD:
                tree = new TallRedwoodTree(random, delegate);
                break;
            case MEGA_REDWOOD:
                tree = new RedwoodTree(random, delegate);
                break;
            case BIRCH:
                tree = new BirchTree(random, delegate);
                break;
            case TALL_BIRCH:
                tree = new BirchTree(random, true, delegate);
                break;
            case JUNGLE:
                tree = new JungleTree(random, delegate);
                break;
            case SMALL_JUNGLE:
                tree = new GenericTree(random, random.nextInt(7) + 4, 3, 3, delegate);
                break;
            case COCOA_TREE:
                tree = new GenericTree(random, random.nextInt(7) + 4, 3, 3, true, delegate);
                break;
            case JUNGLE_BUSH:
                tree = new JungleBush(random, delegate);
                break;
            case ACACIA:
                tree = new AcaciaTree(random, delegate);
                break;
            case DARK_OAK:
                tree = new DarkOakTree(random, delegate);
                break;
            case BROWN_MUSHROOM:
                tree = new HugeMushroom(random, Material.HUGE_MUSHROOM_1, delegate);
                break;
            case RED_MUSHROOM:
                tree = new HugeMushroom(random, Material.HUGE_MUSHROOM_2, delegate);
                break;
            default:
                return false;
        }

        if (tree.generate(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
            if (forceUpdate) {
                delegate.updateBlockStates();
            }
            return true;
        }

        return false;
    }
}
