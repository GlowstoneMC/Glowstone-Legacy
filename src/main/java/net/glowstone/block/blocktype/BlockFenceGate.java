package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockFenceGate extends DefaultBlockType {

    public BlockFenceGate() {
        super(
                new BlockOpenable() {
                    @Override
                    protected void onOpened(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc, GlowBlockState state, MaterialData materialData) {
                        if (materialData instanceof Gate) {
                            Gate gate = (Gate) materialData;
                            gate.setFacingDirection(getOpenDirection(player.getLocation(), gate.getFacing()));
                        } else {
                            warnMaterialData(Gate.class, materialData);
                        }
                    }
                },
                new BlockDirectional()
        );
    }

    private static BlockFace getOpenDirection(Location loc, BlockFace oldFacing) {
        BlockFace facingDirection = BlockDirectional.getOppositeBlockFace(loc, false);

        if (facingDirection == oldFacing.getOppositeFace()) {
            return facingDirection;
        } else {
            return oldFacing;
        }
    }
}
