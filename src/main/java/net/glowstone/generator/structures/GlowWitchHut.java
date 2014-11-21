package net.glowstone.generator.structures;

import java.util.Random;

import net.glowstone.generator.structures.util.StructureBuilder;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;

public class GlowWitchHut extends GlowTemplePiece {

    private boolean hasWitch;

    public GlowWitchHut() {
        super();
    }

    public GlowWitchHut(Random random, Location location) {
        super(random, location, new Vector(7, 5, 9));
    }

    public void setHasWitch(boolean hasWitch) {
        this.hasWitch = hasWitch;
    }

    public boolean getHasWitch() {
        return hasWitch;
    }

    @Override
    public boolean generate(World world, Random random, BlockStateDelegate delegate) {
        if (!super.generate(world, random, delegate)) {
            return false;
        }

        adjustHorizontalPosition(world);

        final StructureBuilder builder = new StructureBuilder(world, this, delegate);
        builder.fill(new Vector(1, 1, 2), new Vector(5, 4, 7), Material.WOOD, 1, Material.AIR); // hut body
        builder.fill(new Vector(1, 1, 1), new Vector(5, 1, 1), Material.WOOD, 1); // hut steps
        builder.fill(new Vector(2, 1, 0), new Vector(4, 1, 0), Material.WOOD, 1); // hut steps
        builder.fill(new Vector(4, 2, 2), new Vector(4, 3, 2), Material.AIR); // hut door
        builder.fill(new Vector(5, 3, 4), new Vector(5, 3, 5), Material.AIR); // left window
        builder.setBlock(new Vector(1, 3, 4), Material.AIR);
        builder.setBlock(new Vector(1, 3, 5), Material.FLOWER_POT, 7);
        builder.setBlock(new Vector(2, 3, 2), Material.FENCE);
        builder.setBlock(new Vector(3, 3, 7), Material.FENCE);

        final Stairs stairsN = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsN.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
        builder.fill(new Vector(0, 4, 1), new Vector(6, 4, 1), stairsN.getItemType(), stairsN);
        final Stairs stairsE = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsE.setFacingDirection(getRelativeFacing(BlockFace.WEST));
        builder.fill(new Vector(6, 4, 2), new Vector(6, 4, 7), stairsE.getItemType(), stairsE);
        final Stairs stairsS = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsS.setFacingDirection(getRelativeFacing(BlockFace.NORTH));
        builder.fill(new Vector(0, 4, 8), new Vector(6, 4, 8), stairsS.getItemType(), stairsS);
        final Stairs stairsW = new Stairs(Material.SPRUCE_WOOD_STAIRS);
        stairsW.setFacingDirection(getRelativeFacing(BlockFace.EAST));
        builder.fill(new Vector(0, 4, 2), new Vector(0, 4, 7), stairsW.getItemType(), stairsW);

        builder.fill(new Vector(1, 0, 2), new Vector(1, 3, 2), Material.LOG);
        builder.fill(new Vector(5, 0, 2), new Vector(5, 3, 2), Material.LOG);
        builder.fill(new Vector(1, 0, 7), new Vector(1, 3, 7), Material.LOG);
        builder.fill(new Vector(5, 0, 7), new Vector(5, 3, 7), Material.LOG);

        builder.setBlock(new Vector(1, 2, 1), Material.FENCE);
        builder.setBlock(new Vector(5, 2, 1), Material.FENCE);

        builder.setBlock(new Vector(4, 2, 6), Material.CAULDRON);
        builder.setBlock(new Vector(3, 2, 6), Material.WORKBENCH);

        builder.setBlockDownward(new Vector(1, -1, 2), Material.LOG);
        builder.setBlockDownward(new Vector(5, -1, 2), Material.LOG);
        builder.setBlockDownward(new Vector(1, -1, 7), Material.LOG);
        builder.setBlockDownward(new Vector(5, -1, 7), Material.LOG);

        if (!hasWitch) {
            // TODO: uncomment this later
            // hasWitch = builder.spawnMob(new Vector(2, 2, 5), EntityType.WITCH);
            // I believe vanilla 1.8 tries to spawn the witch on different floor levels
        }

        return true;
    }
}
