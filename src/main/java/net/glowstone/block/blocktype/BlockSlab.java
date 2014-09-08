package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;
import org.bukkit.util.Vector;

public class BlockSlab extends BlockType {
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        if (state.getBlock().getType() == Material.STEP) {
            state.setType(Material.DOUBLE_STEP);
            state.setRawData((byte) holding.getDurability());
            return;
        }
        if (state.getBlock().getType() == Material.WOOD_STEP) {
            state.setType(Material.WOOD_DOUBLE_STEP);
            state.setRawData((byte) holding.getDurability());
            return;
        }

        super.placeBlock(player, state, face, holding, clickedLoc);

        if ((face == BlockFace.DOWN) || ((face != BlockFace.UP) && (clickedLoc.getY() >= 8.0D))) {
            MaterialData data = state.getData();
            if ((data instanceof Step))
                ((Step) data).setInverted(true);
            else if ((data instanceof WoodenStep)) {
                ((WoodenStep) data).setInverted(true);
            }
            state.setData(data);
        }
    }

    public static SlabPlaceable isSlabPlaceable(GlowPlayer player, GlowBlock target, GlowBlock against, BlockFace face, ItemStack holding) {
        if ((holding.getType() == Material.STEP) || (holding.getType() == Material.WOOD_STEP)) {
            byte againstData = against.getData();
            byte holdingData = (byte) holding.getDurability();

            if ((against.getType() == holding.getType()) && (((face == BlockFace.UP) && (againstData == holdingData)) || ((face == BlockFace.DOWN) && (againstData - 8 == holdingData)))) {
                return SlabPlaceable.AGAINSTBLOCK;
            }
            if ((target.getType() == holding.getType()) && (target.getData() % 8 == holdingData)) {
                return SlabPlaceable.TARGETBLOCK;
            }
        }
        return SlabPlaceable.NO;
    }

    public static enum SlabPlaceable {
        NO, TARGETBLOCK, AGAINSTBLOCK;
    }
}
