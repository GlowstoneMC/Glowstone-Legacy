package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockFenceGate extends BlockOpenable {
    private static final BlockFace[] faces = {BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH};

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        MaterialData materialData = state.getData();
        if (materialData instanceof Gate) {
            Gate gate = (Gate) materialData;
            float yaw = player.getLocation().getYaw();
            gate.setFacingDirection(blockFaceFromYaw(yaw));
            state.update(true);
        } else {
            warnMaterialData(Gate.class, materialData);
        }
    }

    private static BlockFace blockFaceFromYaw(float yaw) {
        yaw = yaw % 360;
        if (yaw < 0)
            yaw += 360;
        return faces[Math.round(yaw / 90f) & 0x3];
    }

    @Override
    protected void onOpened(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc, GlowBlockState state, MaterialData materialData) {
        if (materialData instanceof Gate) {
            Gate gate = (Gate) materialData;
            gate.setFacingDirection(getOpenDirection(player.getLocation().getYaw(), gate.getFacing()));
        } else {
            warnMaterialData(Gate.class, materialData);
        }
    }

    private static BlockFace getOpenDirection(float yaw, BlockFace oldFacing) {
        BlockFace facingDirection = blockFaceFromYaw(yaw);

        if (facingDirection == oldFacing.getOppositeFace()) {
            return facingDirection;
        } else {
            return oldFacing;
        }
    }
}
