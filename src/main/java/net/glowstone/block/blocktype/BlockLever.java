package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockLever extends DefaultBlockType {
    private final BlockType directional = new BlockDirectional();
    private final BlockType attachable = new BlockAttachable();

    public BlockLever() {
        super(new BlockDropWithoutData());
    }

    @Override
    public Boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        final GlowBlockState state = block.getState();
        final MaterialData data = state.getData();

        if (!(data instanceof Lever)) {
            warnMaterialData(Lever.class, data);
            return false;
        }

        final Lever lever = (Lever) data;
        lever.setPowered(!lever.isPowered());
        state.update();
        return true;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector
            clickedLoc) {
        switch (face) {
            case UP:
            case DOWN:
                attachable.placeBlock(player, state, face, holding, clickedLoc);
                directional.placeBlock(player, state, face, holding, clickedLoc);
                break;
            default:
                state.setRawData((byte) 0x1); //lever should be on block's side
                attachable.placeBlock(player, state, face, holding, clickedLoc);
                break;
        }
    }
}
