package net.glowstone.block.blocktype;

import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Ladder;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockLadder extends BlockClimbable {

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);
        GlowWorld world = state.getWorld();

        MaterialData data = state.getData();
        if (data instanceof Ladder) {
            if (world.isBlockSideSolid(state.getLocation(), face.getOppositeFace())) {
                ((Ladder) data).setFacingDirection(face.getOppositeFace());
            } else {
                if (world.isBlockSideSolid(state.getLocation(), BlockFace.SOUTH)) {
                    ((Ladder) data).setFacingDirection(BlockFace.SOUTH);
                } else if (world.isBlockSideSolid(state.getLocation(), BlockFace.WEST)) {
                    ((Ladder) data).setFacingDirection(BlockFace.WEST);
                } else if (world.isBlockSideSolid(state.getLocation(), BlockFace.NORTH)) {
                    ((Ladder) data).setFacingDirection(BlockFace.NORTH);
                } else if (world.isBlockSideSolid(state.getLocation(), BlockFace.EAST)) {
                    ((Ladder) data).setFacingDirection(BlockFace.EAST);
                } else {
                    return;
                }
            }

            state.setData(data);
        } else {
            warnMaterialData(Ladder.class, data);
        }
    }
}
