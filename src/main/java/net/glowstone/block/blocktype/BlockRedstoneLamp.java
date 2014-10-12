package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class BlockRedstoneLamp extends BlockType {

    private final boolean isLampOn;

    public BlockRedstoneLamp(boolean isOn) {
        isLampOn = isOn;
        setDrops(new ItemStack(Material.REDSTONE_LAMP_OFF));
    }

    @Override
    public void traceBlockPower(GlowBlock block, RSManager rsManager, Material srcMat, BlockFace flowDir, int inPower, boolean isDirect) {
        super.traceBlockPower(block, rsManager, srcMat, flowDir, inPower, isDirect);
        rsManager.setBlockPower(block, 15);
        if (!isDirect && srcMat != Material.REDSTONE_WIRE) {
            return;
        }
        BlockFace[] aroundFaces = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        for (BlockFace face : aroundFaces) {
            GlowBlock relative = block.getRelative(face);
            if (relative.getType() == Material.REDSTONE_LAMP_OFF || relative.getType() == Material.REDSTONE_LAMP_ON) {
                rsManager.setBlockPower(relative, 15);
            }
        }
    }

    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        if (power > 0) {
            block.setType(Material.REDSTONE_LAMP_ON);
        } else {
            block.setType(Material.REDSTONE_LAMP_OFF);
        }
    }
}
