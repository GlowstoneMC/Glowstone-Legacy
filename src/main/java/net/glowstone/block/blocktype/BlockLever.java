package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
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
    public boolean canBlockEmitPower(GlowBlock block, BlockFace face, boolean isDirect) {
        
        final MaterialData data = block.getState().getData();
        if (data instanceof Lever) {
            final Lever l = (Lever) data;
            if (!l.isPowered()) {
                return false;
            }
            //The lever powers the attached block direct
            if (l.getAttachedFace().equals(face)) {
                return true;
            } else if (isDirect) { //All other faces aren't powered direct
                return false;
            }
            return true; //Emits power to all directions, but not direct
        }
        return false;
    }
    
    @Override
    public boolean isRedSource(GlowBlock block) {
        return true;
    }
    
    @Override
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
        final MaterialData data = block.getState().getData();
        if (data instanceof Lever) {
            final Lever l = (Lever) data;
            if (l.isPowered()) {
                rsManager.setBlockPower(block, 15);
            } else {
                rsManager.setBlockPower(block, 0);
            }
        }
    }
    
    @Override
    public void traceBlockPowerStart(GlowBlock block, RSManager rsManager) {
        final MaterialData data = block.getState().getData();
        if (data instanceof Lever) {
            final Lever l = (Lever) data;
            if(!l.isPowered()) return;
            // Trace in all directions.
            traceBlockPowerFromLever(block, rsManager, l.getAttachedFace(), BlockFace.UP);
            traceBlockPowerFromLever(block, rsManager, l.getAttachedFace(), BlockFace.DOWN);
            traceBlockPowerFromLever(block, rsManager, l.getAttachedFace(), BlockFace.NORTH);
            traceBlockPowerFromLever(block, rsManager, l.getAttachedFace(), BlockFace.SOUTH);
            traceBlockPowerFromLever(block, rsManager, l.getAttachedFace(), BlockFace.WEST);
            traceBlockPowerFromLever(block, rsManager, l.getAttachedFace(), BlockFace.EAST);
        }
    }
    
    private void traceBlockPowerFromLever(GlowBlock srcBlock, RSManager rsManager, BlockFace directDir, BlockFace toDir) {
        if(toDir.equals(directDir)) {
            rsManager.traceFromBlock(srcBlock, toDir, 15, true);
        } else {
            rsManager.traceFromBlock(srcBlock, toDir, 15, false);
        }
    }
    
    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        if (power == 15) {
            rsManager.addSource(block);
        }
    }

    public void setAttachedFace(final Lever lever, final BlockFace attachedFace) {
        byte data = lever.getData();
        switch (attachedFace) {
            case WEST:
                data |= 1;
                break;
            case EAST:
                data |= 2;
                break;
            case NORTH:
                data |= 3;
                break;
            case SOUTH:
                data |= 4;
                break;
            case DOWN:
                data |= 5; // or 6
                break;
            case UP:
                data |= 7; // or 0
                break;
        }
        lever.setData(data);
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
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
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        final MaterialData data = state.getData();

        if (!(data instanceof Lever)) {
            warnMaterialData(Lever.class, data);
            return;
        }

        final Lever lever = (Lever) data;
        setAttachedFace(state, face.getOppositeFace());
        lever.setFacingDirection(face == BlockFace.UP || face == BlockFace.DOWN ? player.getDirection() : face);

    }
}
