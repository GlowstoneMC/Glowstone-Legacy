package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.util.Vector;

public class BlockTorch extends BlockType {

    private final Material matType;

    public BlockTorch(Material matType) {
        this.matType = matType;
    }

    protected BlockFace getOwnFacing(GlowBlock block) {
        switch(block.getData()) {
            case 1:
                return BlockFace.EAST;
            case 2:
                return BlockFace.WEST;
            case 3:
                return BlockFace.SOUTH;
            case 4:
                return BlockFace.NORTH;
            default:
                return BlockFace.UP;
        }
    }

    protected int getFacing(BlockFace face) {
        switch (face) {
            case EAST:
                return 1;
            case WEST:
                return 2;
            case SOUTH:
                return 3;
            case NORTH:
                return 4;
        }

        return 5;
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        return canPlaceAt(block, against, true);
    }
    
    public boolean canPlaceAt(GlowBlock block, BlockFace against, Boolean withRecursion) {
        GlowBlock againstBlock = block.getRelative(against.getOppositeFace());
        if (against == BlockFace.UP) {
            if (againstBlock != null) {
                Material mat = againstBlock.getType();
                if (mat.isOccluding()) {
                    return true;
                }
                if (mat == Material.GLASS || mat == Material.STAINED_GLASS || mat == Material.FENCE || mat == Material.NETHER_FENCE || mat == Material.COBBLE_WALL || mat == Material.REDSTONE_BLOCK) {
                    return true;
                }
                MaterialData data = againstBlock.getState().getData();
                if (data instanceof Stairs) {
                    if (((Stairs) data).isInverted()) {
                        return true;
                    }
                } else if (data instanceof Step) {
                    if (((Step) data).isInverted()) {
                        return true;
                    }
                } else if (mat == Material.SNOW) {
                    if ((againstBlock.getData() & 0x7) != 0) {
                        return true;
                    }
                }
            }
        }
        BlockFace[] tryAfter = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP};
        if (against == BlockFace.DOWN) {
            if (withRecursion) {
                for (BlockFace face : tryAfter) {
                    if (canPlaceAt(block, face, false)) {
                        return true;
                    }
                }
            }
        } else {
            if (againstBlock != null) {
                Material mat = againstBlock.getType();
                if (mat.isOccluding() || mat == Material.REDSTONE_BLOCK) {
                    return true;
                } else if (withRecursion) {
                    for (BlockFace face : tryAfter) {
                        if (face == against) continue;
                        if (canPlaceAt(block, face, false)) {
                            return true;
                        }
                    }
                }
            } 
        }
        return false;
    }
    
    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        state.setType(matType);
        BlockFace[] tryAfter = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP};
        BlockFace faceNew = face;
        if (face == BlockFace.DOWN) {    
            for (BlockFace tryFace : tryAfter) {
                if (canPlaceAt(state.getBlock(), tryFace, false)) {
                    faceNew = tryFace;
                    break;
                }
            }
        } else if (face != BlockFace.UP) {
            GlowBlock against = state.getBlock().getRelative(face.getOppositeFace());
            if (!against.getType().isOccluding() && against.getType() != Material.REDSTONE_BLOCK) {
                for (BlockFace tryFace : tryAfter) {
                    if (tryFace == face) continue;
                    if (canPlaceAt(state.getBlock(), tryFace, false)) {
                        faceNew = tryFace;
                        break;
                    }
                }
            }
        }
        state.setRawData((byte) getFacing(faceNew));
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(matType, 1, (byte) 0)));
    }
}
