package net.glowstone.generator.objects.trees;

import java.util.Random;

import org.bukkit.Location;

import net.glowstone.util.BlockStateDelegate;

public class TallBirchTree extends BirchTree {

    public TallBirchTree(Random random, Location location, BlockStateDelegate delegate) {
        super(random, location, delegate);
        setHeight(height + random.nextInt(7));
    }
}
