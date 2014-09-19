package net.glowstone.block.blocktype;

import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.util.Vector;

/**
 * Represents blocks that can be opened through a right-click.
 * A block can be opened if it's {@link org.bukkit.material.MaterialData MaterialData} implements {@link org.bukkit.material.Openable Openable}.
 */
public class BlockOpenable extends BlockType {

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        GlowBlockState blockState = block.getState();
        MaterialData materialData = blockState.getData();
        if (materialData instanceof Openable) {
            Openable toOpen = (Openable) materialData;
            boolean wasOpen = toOpen.isOpen();
            toOpen.setOpen(!wasOpen);

            if (wasOpen) {
                onClosed(player, block, face, clickedLoc, blockState, materialData);
            } else {
                onOpened(player, block, face, clickedLoc, blockState, materialData);
            }

            blockState.update(true);
            return true;
        } else {
            GlowServer.logger.warning("Interacting " + getMaterial().name() + ", however this blocks seems not to be openable (" + materialData + ")");
            return false;
        }
    }

    protected void onOpened(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc, GlowBlockState state, MaterialData materialData) {
        //To override in sub-classes
    }

    protected void onClosed(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc, GlowBlockState state, MaterialData materialData) {
        //To override in sub-classes
    }
}
