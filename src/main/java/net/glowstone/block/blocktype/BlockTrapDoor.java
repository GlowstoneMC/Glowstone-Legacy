package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TrapDoor;
import org.bukkit.util.Vector;

public class BlockTrapDoor extends BlockOpenable {
    private static final BlockFace[] faces = {BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH};

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        MaterialData materialData = state.getData();
        if (materialData instanceof TrapDoor) {
            TrapDoor trapDoor = (TrapDoor) materialData;
            trapDoor.setFacingDirection(face);
            if (clickedLoc.getY() >= 7.5)
                trapDoor.setInverted(true);
            else
                trapDoor.setInverted(false);
            state.update(true);
        } else {
            warnMaterialData(TrapDoor.class, materialData);
        }
    }
}
