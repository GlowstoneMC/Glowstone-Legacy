package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.block.GlowBlock;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SimpleAttachableMaterialData;

public class BlockAttachable extends BlockNeedsAttached {
    private final boolean inverted;

    public BlockAttachable() {
        this(false);
    }

    public BlockAttachable(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        MaterialData data = state.getData();

        if (!(data instanceof Attachable)) {
            warnMaterialData(Attachable.class, data);
            return;
        }

        Attachable attachable = (Attachable) data;
        attachable.setFacingDirection(inverted ? face.getOppositeFace() : face);
    }
    @Override
    protected BlockFace getAttachedFace(GlowBlock me) {
        MaterialData data = me.getState().getData();
        if (data instanceof SimpleAttachableMaterialData) {
            return ((SimpleAttachableMaterialData) data).getAttachedFace();
        } else {
            warnMaterialData(SimpleAttachableMaterialData.class, data);
            return BlockFace.DOWN;
        }
    }
}
