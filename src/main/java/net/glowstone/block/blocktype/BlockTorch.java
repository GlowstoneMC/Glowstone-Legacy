package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class BlockTorch extends BlockDirectDrops {

    private final Material matType;

    protected BlockTorch(Material matType, Material dropMatType) {
        super(dropMatType, 0, 1);
        this.matType = matType;
    }

    public BlockTorch(Material matType) {
        this(matType, matType);
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
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        state.setType(matType);
        state.setRawData((byte)getFacing(face));
    }
} 
