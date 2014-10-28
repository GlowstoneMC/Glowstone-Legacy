package net.glowstone.generator.objects.trees;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Vine;

import net.glowstone.util.BlockStateDelegate;

public class JungleTree extends GenericTree {

    protected int logType;
    protected int leavesType;

    public JungleTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setHeight(random.nextInt(20) + random.nextInt(3) + 10);
        logType = 3;
        leavesType = 3;
    }

    @Override
    public boolean canPlaceOn(World world, int x, int y, int z) {
        final BlockState state = delegate.getBlockState(world, x, y, z);
        if (state.getType() != Material.GRASS
                && state.getType() != Material.DIRT) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canPlaceAt(World world, int sourceX, int sourceY, int sourceZ) {
        for (int y = sourceY; y <= sourceY + 1 + height; y++) {
            // Space requirement
            int radius = 2; // default radius if above first block
            if (y == sourceY) {
                radius = 1; // radius at source block y is 1 (only trunk)
            } else if (y >= sourceY + 1 + height - 2) {
                radius = 2; // max radius starting at leaves bottom
            }
            // check for block collision on horizontal slices
            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (y >= 0 && y < 256) {
                        // we can overlap some blocks around
                        final Material type = delegate.getBlockState(world, x, y, z).getType();
                        if (!overridables.contains(type)) {
                            return false;
                        }
                    } else { // height out of range
                        return false;
                    }
                }
            }
        }
        return true;
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

        // generates the canopy leaves
        for (int y = -2; y <= 0; y++) {
            generateLeaves(world, sourceX, sourceY + height + y, sourceZ, 3 - y, false);
        }

        // generates the branches
        int branchHeight = height - 2 - random.nextInt(4);
        while (branchHeight > height / 2) { // branching start at least at middle height
            int x = 0, z = 0;
            // generates a branch
            float d = (float) (random.nextFloat() * Math.PI * 2.0F); // random direction
            for (int i = 0; i < 5; i++) {
                // branches are always longer when facing south or east (positive X or positive Z)
                x = (int) (Math.cos(d) * i + 1.5F);
                z = (int) (Math.sin(d) * i + 1.5F);
                delegate.setTypeAndRawData(world, sourceX + x, sourceY + branchHeight - 3 + i / 2, sourceZ + z, Material.LOG, logType);
            }
            // generates leaves for this branch
            for (int y = branchHeight - (random.nextInt(2) + 1); y <= branchHeight; y++) {
                generateLeaves(world, sourceX + x, sourceY + y, sourceZ + z, 1 - (y - branchHeight), true);
            }
            branchHeight -= random.nextInt(4) + 2;
        }

        // generates the trunk
        generateTrunk(world, sourceX, sourceY, sourceZ);

        // add some vines on the trunk
        addVinesOnTrunk(world, sourceX, sourceY, sourceZ);

        // blocks below trunk are always dirt
        generateDirtBelowTrunk(world, sourceX, sourceY, sourceZ);

        return true;
    }

    protected void generateLeaves(World world, int sourceX, int sourceY, int sourceZ, int radius, boolean odd) {

        int n = 1;
        if (odd) {
            n = 0;
        }
        for (int x = sourceX - radius; x <= sourceX + radius + n; x++) {
            int radiusX = x - sourceX;
            for (int z = sourceZ - radius; z <= sourceZ + radius + n; z++) {
                int radiusZ = z - sourceZ;

                int sqX = radiusX * radiusX;
                int sqZ = radiusZ * radiusZ;
                int sqR = radius * radius;
                int sqXb = (radiusX - n) * (radiusX - n);
                int sqZb = (radiusZ - n) * (radiusZ - n);

                if (sqX + sqZ <= sqR || sqXb + sqZb <= sqR || sqX + sqZb <= sqR || sqXb + sqZ <= sqR) {
                    final Material type = delegate.getBlockState(world, x, sourceY, z).getType();
                    if (type == Material.AIR || type == Material.LEAVES) {
                        delegate.setTypeAndRawData(world, x, sourceY, z, Material.LEAVES, leavesType);
                    }
                }
            }
        }
    }

    protected void generateTrunk(World world, int sourceX, int sourceY, int sourceZ) {
        // SELF, SOUTH, EAST, SOUTH EAST
        for (int y = 0; y < height - 1; y++) {
            Material type = world.getBlockAt(sourceX, sourceY + y, sourceZ).getType();
            if (type == Material.AIR || type == Material.LEAVES) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, Material.LOG, logType);
            }
            type = world.getBlockAt(sourceX, sourceY + y, sourceZ + 1).getType();
            if (type == Material.AIR || type == Material.LEAVES) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ + 1, Material.LOG, logType);
            }
            type = world.getBlockAt(sourceX + 1, sourceY + y, sourceZ).getType();
            if (type == Material.AIR || type == Material.LEAVES) {
                delegate.setTypeAndRawData(world, sourceX + 1, sourceY + y, sourceZ, Material.LOG, logType);
            }
            type = world.getBlockAt(sourceX + 1, sourceY + y, sourceZ + 1).getType();
            if (type == Material.AIR || type == Material.LEAVES) {
                delegate.setTypeAndRawData(world, sourceX + 1, sourceY + y, sourceZ + 1, Material.LOG, logType);
            }
        }
    }

    protected void generateDirtBelowTrunk(World world, int sourceX, int sourceY, int sourceZ) {
        // SELF, SOUTH, EAST, SOUTH EAST
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ + 1, Material.DIRT, 0);
        delegate.setTypeAndRawData(world, sourceX + 1, sourceY - 1, sourceZ, Material.DIRT, 0);
        delegate.setTypeAndRawData(world, sourceX + 1, sourceY - 1, sourceZ + 1, Material.DIRT, 0);
    }

    private void addVinesOnTrunk(World world, int sourceX, int sourceY, int sourceZ) {
        for (int y = 1; y < height; y++) {
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX - 1, sourceY + y, sourceZ).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX - 1, sourceY + y, sourceZ, Material.VINE, new Vine(BlockFace.EAST));
            }
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX, sourceY + y, sourceZ - 1).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX, sourceY + y, sourceZ - 1, Material.VINE, new Vine(BlockFace.SOUTH));
            }
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX + 2, sourceY + y, sourceZ).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX + 2, sourceY + y, sourceZ, Material.VINE, new Vine(BlockFace.WEST));
            }
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX + 1, sourceY + y, sourceZ - 1).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX + 1, sourceY + y, sourceZ - 1, Material.VINE, new Vine(BlockFace.SOUTH));
            }
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX + 2, sourceY + y, sourceZ + 1).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX + 2, sourceY + y, sourceZ + 1, Material.VINE, new Vine(BlockFace.WEST));
            }
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX + 1, sourceY + y, sourceZ + 2).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX + 1, sourceY + y, sourceZ + 2, Material.VINE, new Vine(BlockFace.NORTH));
            }
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX - 1, sourceY + y, sourceZ + 1).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX - 1, sourceY + y, sourceZ + 1, Material.VINE, new Vine(BlockFace.EAST));
            }
            if (random.nextInt(3) != 0 &&
                    delegate.getBlockState(world, sourceX, sourceY + y, sourceZ + 2).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX, sourceY + y, sourceZ + 2, Material.VINE, new Vine(BlockFace.NORTH));
            }
        }
    }
}
