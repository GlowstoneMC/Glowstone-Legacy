package net.glowstone.block.itemtype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class ItemFilledBucket extends ItemPlaceAs {

    public ItemFilledBucket (Material placeAs) {
        super(placeAs);
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock against, BlockFace face, ItemStack holding, Vector clickedLoc) {
        GlowBlock target = against.getRelative(face);
        BlockType againstBlockType = ItemTable.instance().getBlock(against.getType());

        // only allow placement inside replaceable blocks
        if (againstBlockType.canAbsorb(target, face, holding)) {
            target = against;
        } else if (!target.isEmpty()) {
            BlockType targetType = ItemTable.instance().getBlock(target.getTypeId());
            if (!targetType.canOverride(target, face, holding)) {
                return;
            }
        }

        GlowBlockState newState = target.getState();

        PlayerBucketEmptyEvent emptyBucketEvent = EventFactory.onPlayerBucketEmpty(player, target, face, holding.getType(), holding);
        if (emptyBucketEvent.isCancelled()) {
            return;
        }

        this.getPlaceAs().placeBlock(player, newState, face, holding, clickedLoc);

        // perform the block change
        newState.update(true);

        // deduct from stack if not in creative mode
        if (player.getGameMode() != GameMode.CREATIVE) {
            holding.setType(Material.BUCKET);
        }
    }
}
