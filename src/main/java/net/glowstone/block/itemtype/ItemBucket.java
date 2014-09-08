package net.glowstone.block.itemtype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockLiquid;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.Iterator;

public class ItemBucket extends ItemType {

    public ItemBucket () {
        super();
        this.setMaxStackSize(16);
    }

    @Override
    public void rightClickAir(GlowPlayer player, ItemStack holding) {
        Iterator<Block> itr = new BlockIterator(player, 5);
        Block target = null;
        BlockType targetBlockType = null;
        boolean validTarget = false;

        // Find the next available non-air liquid block type which is collectible in a radius of 5 blocks
        while (itr.hasNext()) {
            target = itr.next();
            targetBlockType = ItemTable.instance().getBlock(target.getType());
            if (targetBlockType != null && targetBlockType instanceof BlockLiquid) {
                if (((BlockLiquid)targetBlockType).isCollectible((GlowBlockState) target.getState())) {
                    validTarget = true;
                    break;
                }
            }
        }

        if (target != null && validTarget == true) {
            // TODO: Do some math to get the actual block face.
            BlockFace face = BlockFace.SELF;
            Material replaceWith = ((BlockLiquid) targetBlockType).getBucketType();

            PlayerBucketFillEvent bucketFill = EventFactory.onPlayerBucketFill(player, target, face, holding.getType(), holding);
            if (bucketFill.isCancelled()) {
                return;
            }

            if (player.getGameMode() != GameMode.CREATIVE) {
                if (holding.getAmount() == 1) {
                    holding.setType(replaceWith);
                }
                else {
                    holding.setAmount(holding.getAmount() - 1);
                    player.getInventory().addItem(new ItemStack(replaceWith));
                }
            }

            target.setType(Material.AIR);
        }
    }
}
