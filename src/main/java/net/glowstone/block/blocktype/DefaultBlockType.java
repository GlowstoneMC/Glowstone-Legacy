package net.glowstone.block.blocktype;

import net.glowstone.EventFactory;
import net.glowstone.GlowChunk;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.ItemTable;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.block.itemtype.DefaultItemType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Collection;

public class DefaultBlockType extends DefaultItemType implements BlockType {

    public DefaultBlockType(BlockType... features) {
        super(features);
    }

    @Override
    protected final BlockType[] getFeatures() {
        return (BlockType[]) super.getFeatures();
    }

    protected void warnMaterialData(Class<? extends MaterialData> enderPortalFrameClass, MaterialData data) {
        //TODO
    }
    //////////////////////
    // Actions

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock against, BlockFace face, ItemStack holding, Vector clickedLoc) {
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

        super.rightClickBlock(player, target, face, holding, clickedLoc);
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        Collection<ItemStack> drops = null;

        for (BlockType feature : getFeatures()) {
            drops = throwDouble(drops, feature.getDrops(block, tool));
        }

        return drops;
    }

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        TileEntity tileEntity = null;

        for (BlockType feature : getFeatures()) {
            tileEntity = throwDouble(tileEntity, feature.createTileEntity(chunk, cx, cy, cz));
        }

        return tileEntity;
    }

    @Override
    public Boolean canPlaceAt(GlowBlock block, BlockFace against) {
        Boolean result = null;

        for (BlockType feature : getFeatures()) {
            result = throwDouble(result, feature.canPlaceAt(block, against));
        }

        return result == null ? true : result;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        state.setType(getMaterial());
        state.setRawData((byte) holding.getDurability());

        for (BlockType feature : getFeatures())
            feature.placeBlock(player, state, face, holding, clickedLoc);
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        for (BlockType feature : getFeatures())
            feature.afterPlace(player, block, holding);
    }

    @Override
    public Boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        Boolean result = null;

        for (BlockType feature : getFeatures())
            result = throwDouble(result, feature.blockInteract(player, block, face, clickedLoc));

        return result == null ? false : result;
    }

    @Override
    public void blockDestroy(GlowPlayer player, GlowBlock block, BlockFace face) {
        for (BlockType feature : getFeatures())
            feature.blockDestroy(player, block, face);
    }

    @Override
    public Boolean canAbsorb(GlowBlock block, BlockFace face, ItemStack holding) {
        Boolean result = null;

        for (BlockType feature : getFeatures())
            result = throwDouble(result, feature.canAbsorb(block, face, holding));

        return result == null ? false : result;
    }

    @Override
    public Boolean canOverride(GlowBlock block, BlockFace face, ItemStack holding) {
        Boolean result = null;

        for (BlockType feature : getFeatures())
            result = throwDouble(result, feature.canOverride(block, face, holding));


        return result == null ? block.isLiquid() : result;
    }
}
