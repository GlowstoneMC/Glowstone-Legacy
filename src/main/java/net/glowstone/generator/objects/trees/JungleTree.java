package net.glowstone.generator.objects.trees;

import java.util.Random;

import org.bukkit.Location;

import net.glowstone.util.BlockStateDelegate;

public class JungleTree extends GenericTree {

    public JungleTree(Random random, Location location, BlockStateDelegate delegate) {
        super(random, location, delegate);
        setHeight(random.nextInt(7) + 4);
        setTypes(3, 3);
    }
}
