package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockLever extends BlockAttachable {

    public BlockLever() {
        setDrops(new ItemStack(Material.LEVER));
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        final GlowBlockState state = block.getState();
        final MaterialData data = state.getData();
        if (data instanceof Lever) {
            final Lever l = (Lever) data;
            l.setPowered(!l.isPowered());
            state.setData(l);
            state.update();
            return true;
        } else {
            warnMaterialData(Lever.class, data);
            return false;
        }
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        final MaterialData data = state.getData();
        if (data instanceof Lever) {
            final Lever lever = (Lever) data;
            setAttachedFace(state, face.getOppositeFace());
            lever.setFacingDirection(face == BlockFace.UP || face == BlockFace.DOWN ? player.getDirection() : face);
            state.setData(lever);
        } else {
            warnMaterialData(Lever.class, data);
        }
    }
}
