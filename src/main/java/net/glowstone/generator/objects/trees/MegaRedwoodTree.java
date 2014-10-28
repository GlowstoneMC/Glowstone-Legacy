package net.glowstone.generator.objects.trees;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.World;

public class MegaRedwoodTree extends JungleTree {

    public MegaRedwoodTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setHeight(random.nextInt(15) + random.nextInt(3) + 13);
        logType = 1;
        leavesType = 1;
    }

    @Override
    public boolean generate(World world, int sourceX, int sourceY, int sourceZ) {

        // check height range
        if (!canHeightFitAt(sourceY)) {
            return false;
        }

        // check below block
        if (!canPlaceOn(world, sourceX, sourceY - 1, sourceZ)) {
            return false;
        }

        // check for sufficient space around
        if (!canPlaceAt(world, sourceX, sourceY, sourceZ)) {
            return false;
        }

        // generates the leaves
        int leavesHeight = random.nextInt(5);
        if (random.nextBoolean())
            leavesHeight += 3;
        else {
            leavesHeight += 13;
        }
        int previousRadius = 0;
        for (int y = sourceY + height - leavesHeight; y <= sourceY + height; y++) {
            int n = sourceY + height - y;
            int radius = (int) Math.floor(((float) n / (float) leavesHeight) * 3.5F);
            if (radius == previousRadius && n > 0 && y % 2 == 0) {
                radius++;
            }
            generateLeaves(world, sourceX, y, sourceZ, radius, false);
            previousRadius = radius;
        }

        // generates the trunk
        generateTrunk(world, sourceX, sourceY, sourceZ);

        // blocks below trunk are always dirt
        generateDirtBelowTrunk(world, sourceX, sourceY, sourceZ);

        return true;
    }
}
