package net.glowstone.block.blocktype;

import net.glowstone.EventFactory;
import net.glowstone.GlowChunk;
import net.glowstone.GlowServer;
import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.ItemTable;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.block.itemtype.ItemType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
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
     * @param tool The tool used or {@code null} if fists or no tool was used.
     * @return The drops that should be returned.
     */
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
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

    /**
     * Called when a player attempts to destroy a block.
     * @param player The player interacting
     * @param block The block the player destroyed
     * @param face The block face
     */
    public void blockDestroy(GlowPlayer player, GlowBlock block, BlockFace face) {
        // do nothing
    }

    /**
     * Called when a player attempts to place a block on an existing block of
     * this type. Used to determine if the placement should occur into the air
     * adjacent to the block (normal behavior), or absorbed into the block
     * clicked on.
     * @param block The block the player right-clicked
     * @param face The face on which the click occurred
     * @param holding The ItemStack the player was holding
     * @return Whether the place should occur into the block given.
     */
    public boolean canAbsorb(GlowBlock block, BlockFace face, ItemStack holding) {
        return false;
    }

    /**
     * Called to check if this block can be overridden by a block place
     * which would occur inside it.
     * @param block The block being targeted by the placement
     * @param face The face on which the click occurred
     * @param holding The ItemStack the player was holding
     * @return Whether this block can be overridden.
     */
    public boolean canOverride(GlowBlock block, BlockFace face, ItemStack holding) {
        return block.isLiquid();
    }

    @Override
    public final void rightClickBlock(GlowPlayer player, GlowBlock against, BlockFace face, ItemStack holding, Vector clickedLoc) {
        GlowBlock target = against.getRelative(face);

        // check whether the block clicked against should absorb the placement
        BlockType againstType = ItemTable.instance().getBlock(against.getTypeId());
        if (againstType.canAbsorb(against, face, holding)) {
            target = against;
        } else if (!target.isEmpty()) {
            // air can always be overridden
            BlockType targetType = ItemTable.instance().getBlock(target.getTypeId());
            if (!targetType.canOverride(target, face, holding)) {
                return;
            }
        }

        // call canBuild event
        boolean canBuild = canPlaceAt(target, face);
        BlockCanBuildEvent canBuildEvent = new BlockCanBuildEvent(target, getId(), canBuild);
        if (!EventFactory.callEvent(canBuildEvent).isBuildable()) {
            //revert(player, target);
            return;
        }

        // grab states and update block
        GlowBlockState oldState = target.getState(), newState = target.getState();
        placeBlock(player, newState, face, holding, clickedLoc);
        newState.update(true);

        // call blockPlace event
        BlockPlaceEvent event = new BlockPlaceEvent(target, oldState, against, holding, player, canBuild);
        EventFactory.callEvent(event);
        if (event.isCancelled() || !event.canBuild()) {
            oldState.update(true);
            return;
        }

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

    ////////////////////////////////////////////////////////////////////////////
    // Redstone

    /**
     * Determines whether redstone can be emitted from this face or not.
     * @param block The block we are operating on.
     * @param face The face of the block that is being touched, or SELF for internal charge.
     * @param isDirect Whether we are looking for direct or indirect power.
     * @return If this block is able to emit power
     */
    public boolean canBlockEmitPower(GlowBlock block, BlockFace face, boolean isDirect) {
        Material mat = getMaterial();
        return (mat != null && mat.isOccluding());
    }

    /**
     * Checks if this block is a suitable source to add to an RSManager initially.
     * This function is called on chunk loads.
     * @param block The block we are querying.
     * @return Returns if this block can act as a redstone source
     */
    public boolean isRedSource(GlowBlock block) {
        return false;
    }

    /**
     * If needed, this method can be overriden to provide a different power level
     * from the one stored in the redstone manager
     * @param block The block to get the power level from
     * @return The block's current power level
     */
    public Integer getBlockPower(GlowBlock block) {
        return null;
    }

    /**
     * Update redstone state at the end of a redstone pulse.
     * @param block The block we are modifying.
     * @param rsManager The RSManager used for tracking.
     * @param power The final power level.
     */
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
    }

    /**
     * Trace a redstone pulse from a solid source.
     * @param srcBlock The block we are flowing from.
     * @param rsManager The RSManager used for tracking.
     * @param toDir The direction of redstone flow from this block.
     * @param isDirect Whether we are applying direct or indirect power.
     */
    private void traceBlockPowerStartSolid(GlowBlock srcBlock, RSManager rsManager, BlockFace toDir, boolean isDirect) {
        // Get the destination block and ensure that it is suitable.
        GlowBlock destBlock = srcBlock.getRelative(toDir);
        if (destBlock == null) {
            return;
        }
        Material destMat = destBlock.getType();
        if (destMat == Material.REDSTONE_WIRE) {
            // Will trace
        } else {
            return;
        }

        // Trace to target.
        rsManager.traceFromBlock(srcBlock, toDir, 15, isDirect);
    }

    /**
     * Trace a redstone pulse from a solid.
     * NOTE: This function can be extended to cater for inPower and isDirect, need-permitting.
     * @param srcBlock The block we are flowing from.
     * @param rsManager The RSManager used for tracking.
     * @param forbidDir The direction we cannot flow in due to it leading back to our source.
     * @param toDir The direction of redstone flow from this block.
     * @param isDirect Whether we are applying direct or indirect power.
     */
    private void traceBlockPowerSolidToBlock(GlowBlock srcBlock, RSManager rsManager, BlockFace forbidDir, BlockFace toDir, boolean isDirect) {
        // Get the forbidDir check out of the way.
        if (forbidDir == toDir) {
            return;
        }

        // Get the destination block and ensure that it is suitable.
        GlowBlock destBlock = srcBlock.getRelative(toDir);
        if (destBlock == null) {
            return;
        }
        Material destMat = destBlock.getType();
        Material[] trace = new Material[] {Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON};
        if (!Arrays.asList(trace).contains(destMat)) {
            return;
        }

        // Trace to target.
        rsManager.traceFromBlock(srcBlock, toDir, 1, true);
    }

    /**
     * Prepare for a redstone trace.
     * @param block The block we are operating on.
     * @param rsManager The RSManager used for tracking.
     */
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
    }

    /**
     * Begin tracing a redstone pulse as a source.
     * @param block The block we are operating on.
     * @param rsManager The RSManager used for tracking.
     */
    public void traceBlockPowerStart(GlowBlock block, RSManager rsManager) {
        // Check if solid
        if (block.getType() == null || !block.getType().isOccluding()) {
            return;
        }

        traceBlockPowerStartSolid(block, rsManager, BlockFace.UP, false);
        traceBlockPowerStartSolid(block, rsManager, BlockFace.DOWN, false);
        traceBlockPowerStartSolid(block, rsManager, BlockFace.NORTH, false);
        traceBlockPowerStartSolid(block, rsManager, BlockFace.SOUTH, false);
        traceBlockPowerStartSolid(block, rsManager, BlockFace.WEST, false);
        traceBlockPowerStartSolid(block, rsManager, BlockFace.EAST, false);
    }

    /**
     * Trace a redstone pulse from an external source.
     * @param block The block we are operating on.
     * @param rsManager The RSManager used for tracking.
     * @param srcMat The material this pulse is coming from.
     * @param flowDir The direction of redstone flow towards this block.
     * @param inPower The input power level.
     * @param isDirect Whether we are applying direct or indirect power.
     */
    public void traceBlockPower(GlowBlock block, RSManager rsManager, Material srcMat, BlockFace flowDir, int inPower, boolean isDirect) {
        // Check if solid
        if (block.getType() == null || !block.getType().isOccluding()) {
            return;
        }

        // Ensure directness
        if (isDirect) {
            rsManager.addSource(block);
        } else {
            if (srcMat != Material.REDSTONE_WIRE) {
                return;
            }
        }

        // Spread to neighbours
        BlockFace oppDir = flowDir.getOppositeFace();
        traceBlockPowerSolidToBlock(block, rsManager, oppDir, BlockFace.UP, isDirect);
        //traceBlockPowerSolidToBlock(block, rsManager, oppDir, BlockFace.DOWN, isDirect);
        traceBlockPowerSolidToBlock(block, rsManager, oppDir, BlockFace.NORTH, isDirect);
        traceBlockPowerSolidToBlock(block, rsManager, oppDir, BlockFace.SOUTH, isDirect);
        traceBlockPowerSolidToBlock(block, rsManager, oppDir, BlockFace.WEST, isDirect);
        traceBlockPowerSolidToBlock(block, rsManager, oppDir, BlockFace.EAST, isDirect);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Helper methods

    /**
     * Display the warning for finding the wrong MaterialData subclass.
     * @param clazz The expected subclass of MaterialData.
     * @param data The actual MaterialData found.
     */
    protected void warnMaterialData(Class<?> clazz, MaterialData data) {
        GlowServer.logger.warning("Wrong MaterialData for " + getMaterial() + " (" + getClass().getSimpleName() + "): expected " + clazz.getSimpleName() + ", got " + data);
    }

    /**
     * Gets the BlockFace opposite of the direction the location is facing.
     * Usually used to set the way container blocks face when being placed.
     * @param location Location to get opposite of
     * @param inverted If up/down should be used
     * @return Opposite BlockFace or EAST if yaw is invalid
     */
    protected static BlockFace getOppositeBlockFace(Location location, boolean inverted) {
        double rot = location.getYaw() % 360;
        if (inverted) {
            // todo: Check the 67.5 pitch in source. This is based off of WorldEdit's number for this.
            double pitch = location.getPitch();
            if (pitch < -67.5D) {
                return BlockFace.DOWN;
            } else if (pitch > 67.5D) {
                return BlockFace.UP;
            }
        }
        if (rot < 0) {
            rot += 360.0;
        }
        if (0 <= rot && rot < 45) {
            return BlockFace.NORTH;
        } else if (45 <= rot && rot < 135) {
            return BlockFace.EAST;
        } else if (135 <= rot && rot < 225) {
            return BlockFace.SOUTH;
        } else if (225 <= rot && rot < 315) {
            return BlockFace.WEST;
        } else if (315 <= rot && rot < 360.0) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.EAST;
        }
    }
}
