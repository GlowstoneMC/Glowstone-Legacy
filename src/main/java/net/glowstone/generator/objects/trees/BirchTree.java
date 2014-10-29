package net.glowstone.generator.objects.trees;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

public class BirchTree extends GenericTree {

    public BirchTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setHeight(random.nextInt(3) + 5);
        setTypes(2, 2);
    }
}
