package net.glowstone.generator.objects.trees;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

public class JungleTree extends GenericTree {

    public JungleTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setHeight(random.nextInt(7) + 4);
        setTypes(3, 3);
    }
}
