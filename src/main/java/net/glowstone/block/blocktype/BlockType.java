package net.glowstone.block.blocktype;

import net.glowstone.EventFactory;
import net.glowstone.GlowChunk;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.block.itemtype.ItemType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base class for specific types of blocks.
 */
public class BlockType extends ItemType {

    protected List<ItemStack> drops = null;

    ////////////////////////////////////////////////////////////////////////////
    // Setters for subclass use

    protected final void setDrops(ItemStack... drops) {
        this.drops = Arrays.asList(drops);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public accessors

    /**
     * Get the items that will be dropped by digging the block.
     * @param block The block being dug.
     * @return The drops that should be returned.
     */
    public Collection<ItemStack> getDrops(GlowBlock block) {
        if (drops == null) {
            // default calculation
            return Arrays.asList(new ItemStack(block.getType(), 1, block.getData()));
        } else {
            return Collections.unmodifiableList(drops);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Actions

    /**
     * Create a new tile entity at the given location.
     * @param chunk The chunk to create the tile entity at.
     * @param cx The x coordinate in the chunk.
     * @param cy The y coordinate in the chunk.
     * @param cz The z coordinate in the chunk.
     * @return The new TileEntity, or null if no tile entity is used.
     */
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return null;
    }

    /**
     * Check whether the block can be placed at the given location.
     * @param block The location the block is being placed at.
     * @param against The face the block is being placed against.
     * @return Whether the placement is valid.
     */
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        return true;
    }

    /**
     * Called when a block is placed to calculate what the block will become.
     * @param player the player who placed the block
     * @param state the BlockState to edit
     * @param holding the ItemStack that was being held
     * @param face the face off which the block is being placed
     * @param clickedLoc where in the block the click occurred
     */
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        state.setType(getMaterial());
        state.setRawData((byte) holding.getDurability());
    }

    /**
     * Called after a block has been placed by a player.
     * @param player the player who placed the block
     * @param block the block that was placed
     * @param holding the the ItemStack that was being held
     */
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        // do nothing
    }

    /**
     * Called when a player attempts to interact with (right-click) a block of
     * this type already in the world.
     * @param player the player interacting
     * @param block the block interacted with
     * @param face the clicked face
     * @param clickedLoc where in the block the click occurred
     * @return Whether the interaction occurred.
     */
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        return false;
    }

    @Override
    public final void rightClickBlock(GlowPlayer player, GlowBlock against, BlockFace face, ItemStack holding, Vector clickedLoc) {
        GlowBlock target = against.getRelative(face);
        GlowBlockState newState = target.getState();
        BlockSlab.SlabPlaceable slabplaceable = BlockSlab.isSlabPlaceable(player, target, against, face, holding);

        // only allow placement inside tall-grass, air, or liquid
        if (against.getType() == Material.LONG_GRASS || slabplaceable == BlockSlab.SlabPlaceable.AGAINSTBLOCK) {
            target = against;
            newState = target.getState();
        } else if (!target.isEmpty() && !target.isLiquid() && slabplaceable == BlockSlab.SlabPlaceable.NO) {
            //revert(player, target);
            return;
        }

        // call canBuild event
        if (!EventFactory.onBlockCanBuild(target, getId(), face).isBuildable()) {
            //revert(player, target);
            return;
        }

        // calculate new block
        placeBlock(player, newState, face, holding, clickedLoc);

        // call blockPlace event
        BlockPlaceEvent event = EventFactory.onBlockPlace(target, newState, against, player);
        if (event.isCancelled() || !event.canBuild()) {
            //revert(player, target);
            return;
        }

        // perform the block change
        newState.update(true);

        // play a sound effect
        // todo: vary sound effect based on block type
        target.getWorld().playSound(target.getLocation(), Sound.DIG_WOOD, 1, 1);

        // do any after-place actions
        afterPlace(player, target, holding);

        // deduct from stack if not in creative mode
        if (player.getGameMode() != GameMode.CREATIVE) {
            holding.setAmount(holding.getAmount() - 1);
        }
    }
}
