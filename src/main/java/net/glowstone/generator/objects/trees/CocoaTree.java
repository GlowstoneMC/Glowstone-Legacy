package net.glowstone.generator.objects.trees;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Vine;
import org.bukkit.material.CocoaPlant.CocoaPlantSize;

import net.glowstone.util.BlockStateDelegate;

public class CocoaTree extends JungleTree {

    public CocoaTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
    }

    @Override
    public boolean generate(World world, int sourceX, int sourceY, int sourceZ) {
        if (!super.generate(world, sourceX, sourceY, sourceZ)) {
            return false;
        }

        // places some vines on the trunk
        addVinesOnTrunk(world, sourceX, sourceY, sourceZ);
        // search for air around leaves to grow hanging vines
        addVinesOnLeaves(world, sourceX, sourceY, sourceZ);
        // and maybe place some cocoa
        addCocoa(world, sourceX, sourceY, sourceZ);

        return true;
    }

    private void addVinesOnTrunk(World world, int sourceX, int sourceY, int sourceZ) {
        for (int y = 1; y < height; y++) {
            if (random.nextInt(3) != 0
                    && delegate.getBlockState(world, sourceX - 1, sourceY + y, sourceZ).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX - 1, sourceY + y, sourceZ, Material.VINE, new Vine(BlockFace.EAST));
            }
            if (random.nextInt(3) != 0
                    && delegate.getBlockState(world, sourceX + 1, sourceY + y, sourceZ).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX + 1, sourceY + y, sourceZ, Material.VINE, new Vine(BlockFace.WEST));
            }
            if (random.nextInt(3) != 0
                    && delegate.getBlockState(world, sourceX, sourceY + y, sourceZ - 1).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX, sourceY + y, sourceZ - 1, Material.VINE, new Vine(BlockFace.SOUTH));
            }
            if (random.nextInt(3) != 0
                    && delegate.getBlockState(world, sourceX, sourceY + y, sourceZ + 1).getType() == Material.AIR) {
                delegate.setTypeAndData(world, sourceX, sourceY + y, sourceZ + 1, Material.VINE, new Vine(BlockFace.NORTH));
            }
        }
    }

    protected void addVinesOnLeaves(World world, int sourceX, int sourceY, int sourceZ) {
        for (int y = sourceY - 3 + height; y <= sourceY + height; y++) {
            int nY = y - (sourceY + height);
            int radius = 2 - nY / 2;
            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (delegate.getBlockState(world, x, y, z).getType() == Material.LEAVES) {
                        if (random.nextInt(4) == 0
                                && delegate.getBlockState(world, x - 1, y, z).getType() == Material.AIR) {
                            addHangingVine(world, x - 1, y, z, BlockFace.EAST);
                        }
                        if (random.nextInt(4) == 0
                                && delegate.getBlockState(world, x + 1, y, z).getType() == Material.AIR) {
                            addHangingVine(world, x + 1, y, z, BlockFace.WEST);
                        }
                        if (random.nextInt(4) == 0
                                && delegate.getBlockState(world, x, y, z - 1).getType() == Material.AIR) {
                            addHangingVine(world, x, y, z - 1, BlockFace.SOUTH);
                        }
                        if (random.nextInt(4) == 0
                                && delegate.getBlockState(world, x, y, z + 1).getType() == Material.AIR) {
                            addHangingVine(world, x, y, z + 1, BlockFace.NORTH);
                        }
                    }
                }
            }
        }
    }

    private void addHangingVine(World world, int x, int y, int z, BlockFace face) {
        for (int i = 0; i < 5; i++) {
            if (delegate.getBlockState(world, x, y - i, z).getType() != Material.AIR) {
                break;
            }
            delegate.setTypeAndData(world, x, y - i, z, Material.VINE, new Vine(face));
        }
    }

    private void addCocoa(World world, int sourceX, int sourceY, int sourceZ) {
        if (height > 5 && random.nextInt(5) == 0) {
            for (int y = 0; y < 2; y++) {
                for (int i = 0; i < 4; i++) {         // rotate the 4 trunk faces
                    if (random.nextInt(4 - y) == 0) { // higher it is, more chances there is
                        final BlockFace face = getCocoaFace(i);
                        final CocoaPlantSize size = getCocoaSize(random.nextInt(3));
                        final Block block = delegate.getBlockState(world, sourceX, sourceY + height - 5 + y, sourceZ)
                            .getBlock().getRelative(face);
                        delegate.setTypeAndData(world, block.getX(), block.getY(), block.getZ(),
                                Material.COCOA, new CocoaPlant(size, face.getOppositeFace()));
                    }
                }
            }
        }
    }

    private BlockFace getCocoaFace(int n) {
        switch (n) {
            case 0:
                return BlockFace.NORTH;
            case 1:
                return BlockFace.EAST;
            case 2:
                return BlockFace.SOUTH;
            case 3:
                return BlockFace.WEST;
            default:
                return BlockFace.NORTH;
        }
    }

    private CocoaPlantSize getCocoaSize(int n) {
        switch (n) {
            case 0:
                return CocoaPlantSize.SMALL;
            case 1:
                return CocoaPlantSize.MEDIUM;
            case 2:
                return CocoaPlantSize.LARGE;
            default:
                return CocoaPlantSize.SMALL;
        }
    }
}
