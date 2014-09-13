package net.glowstone.block.blocktype;

import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockFenceGate extends BlockOpenable {

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        boolean changed = super.blockInteract(player, block, face, clickedLoc);

        if (changed) {
            BlockState state = block.getState();
            MaterialData data = state.getData();
            if (data instanceof Gate) {
                Gate gate = (Gate) data;
                gate.setFacingDirection(getOpenDirection(player, gate.getFacing()));
                state.update(true);
            } else {
                warnMaterialData(Gate.class, data);
            }
        }

        return changed;
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
