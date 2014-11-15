package net.glowstone.generator.structures;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;

import net.glowstone.util.BlockStateDelegate;

public class WitchHut extends Structure {

    public WitchHut(Random random, Location location, BlockStateDelegate delegate) {
        super(random, location, delegate);
        setSize(new Vector(7, 5, 9));
    }

    @Override
    public boolean generate() {

        int sumY = 0, blockCount = 0;
        for (int x = 0; x < getSize().getBlockX(); x++) {
            for (int z = 0; z < getSize().getBlockZ(); z++) {
                sumY += Math.max(location.getWorld().getSeaLevel(),
                        location.getWorld().getHighestBlockYAt(cuboid.getMin().getBlockX() + x, cuboid.getMin().getBlockZ() + z) + 1);
                blockCount++;
            }
        }
        cuboid.offset(new Vector(0, (sumY / blockCount) - cuboid.getMin().getBlockY(), 0));

        fill(new Vector(1, 1, 2), new Vector(5, 4, 7), Material.WOOD, 1, Material.AIR); // hut body
        fill(new Vector(1, 1, 1), new Vector(5, 1, 1), Material.WOOD, 1); // hut steps
        fill(new Vector(2, 1, 0), new Vector(4, 1, 0), Material.WOOD, 1); // hut steps
        fill(new Vector(4, 2, 2), new Vector(4, 3, 2), Material.AIR); // hut door
        fill(new Vector(5, 3, 4), new Vector(5, 3, 5), Material.AIR); // left window
        setBlock(new Vector(1, 3, 4), Material.AIR);
        setBlock(new Vector(1, 3, 5), Material.FLOWER_POT, 7);
        setBlock(new Vector(2, 3, 2), Material.FENCE);
        setBlock(new Vector(3, 3, 7), Material.FENCE);

        final Stairs stairsN = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsN.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
        fill(new Vector(0, 4, 1), new Vector(6, 4, 1), stairsN.getItemType(), stairsN);
        final Stairs stairsE = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsE.setFacingDirection(getRelativeFacing(BlockFace.WEST));
        fill(new Vector(6, 4, 2), new Vector(6, 4, 7), stairsE.getItemType(), stairsE);
        final Stairs stairsS = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsS.setFacingDirection(getRelativeFacing(BlockFace.NORTH));
        fill(new Vector(0, 4, 8), new Vector(6, 4, 8), stairsS.getItemType(), stairsS);
        final Stairs stairsW = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsW.setFacingDirection(getRelativeFacing(BlockFace.EAST));
        fill(new Vector(0, 4, 2), new Vector(0, 4, 7), stairsW.getItemType(), stairsW);

        fill(new Vector(1, 0, 2), new Vector(1, 3, 2), Material.LOG);
        fill(new Vector(5, 0, 2), new Vector(5, 3, 2), Material.LOG);
        fill(new Vector(1, 0, 7), new Vector(1, 3, 7), Material.LOG);
        fill(new Vector(5, 0, 7), new Vector(5, 3, 7), Material.LOG);

        setBlock(new Vector(1, 2, 1), Material.FENCE);
        setBlock(new Vector(5, 2, 1), Material.FENCE);

        setBlock(new Vector(4, 2, 6), Material.CAULDRON);
        setBlock(new Vector(3, 2, 6), Material.WORKBENCH);

        setBlockDownward(new Vector(1, -1, 2), Material.LOG);
        setBlockDownward(new Vector(5, -1, 2), Material.LOG);
        setBlockDownward(new Vector(1, -1, 7), Material.LOG);
        setBlockDownward(new Vector(5, -1, 7), Material.LOG);

        return true;
    }
}
