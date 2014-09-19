package net.glowstone.block.blocktype;

import net.glowstone.GlowServer;
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
            gate.setFacingDirection(getOpenDirection(player, gate.getFacing()));
        } else {
            warnMaterialData(Gate.class, materialData);
        }
    }

    private static BlockFace getOpenDirection(GlowPlayer player, BlockFace oldFacing) {
        float yaw = (player.getLocation().getYaw() % 360);
        if (yaw < 0) yaw += 360;

        if (oldFacing == BlockFace.NORTH || oldFacing == BlockFace.SOUTH) {
            return yaw < 180 ? BlockFace.SOUTH : BlockFace.NORTH;
        } else if (oldFacing == BlockFace.EAST || oldFacing == BlockFace.WEST) {
            return yaw > 90 && yaw < 270 ? BlockFace.WEST : BlockFace.EAST;
        } else {
            GlowServer.logger.warning("Calling BlockFenceGate#getOpenDirection() with oldFacing == " + oldFacing + ", only N/E/S/W allowed!");
            return oldFacing;
        }
    }
}
