package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BlockButton extends BlockAttachable {

    public BlockButton(Material material) {
        setDrops(new ItemStack(material));
    }

    @Override
    public boolean canBlockEmitPower(GlowBlock block, BlockFace face, boolean isDirect) {

        final MaterialData data = block.getState().getData();
        if (data instanceof Button) {
            final Button l = (Button) data;
            if (!l.isPowered()) {
                return false;
            }
            //The button powers the attached block direct
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
        if (data instanceof Button) {
            final Button l = (Button) data;
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
        if (data instanceof Button) {
            final Button l = (Button) data;
            if (!l.isPowered()) return;
            // Trace in all directions.
            traceBlockPowerFromButton(block, rsManager, l.getAttachedFace(), BlockFace.UP);
            traceBlockPowerFromButton(block, rsManager, l.getAttachedFace(), BlockFace.DOWN);
            traceBlockPowerFromButton(block, rsManager, l.getAttachedFace(), BlockFace.NORTH);
            traceBlockPowerFromButton(block, rsManager, l.getAttachedFace(), BlockFace.SOUTH);
            traceBlockPowerFromButton(block, rsManager, l.getAttachedFace(), BlockFace.WEST);
            traceBlockPowerFromButton(block, rsManager, l.getAttachedFace(), BlockFace.EAST);
        }
    }

    private void traceBlockPowerFromButton(GlowBlock srcBlock, RSManager rsManager, BlockFace directDir, BlockFace toDir) {
        if (toDir.equals(directDir)) {
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

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        final GlowBlockState state = block.getState();
        final MaterialData data = state.getData();

        if (!(data instanceof Button)) {
            warnMaterialData(Button.class, data);
            return false;
        }

        final Button button = (Button) data;

        if (button.isPowered()) {
            return true;
        }

        button.setPowered(true);
        state.update();

        // todo: switch to block scheduling system when one is available
        (new BukkitRunnable() {
            @Override
            public void run() {
                button.setPowered(false);
                state.update();
            }
        }).runTaskLater(null, 20);

        return true;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        MaterialData data = state.getData();

        if (!(data instanceof Button)) {
            warnMaterialData(Button.class, data);
            return;
        }

        setAttachedFace(state, face.getOppositeFace());
    }
}
