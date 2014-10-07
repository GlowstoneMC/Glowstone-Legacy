package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.block.blocktype.IBlockGrowable;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class ItemDye extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        final MaterialData data = holding.getData();
        if (data instanceof Dye) {
            final Dye dye = (Dye) data;
            if (dye.getColor().equals(DyeColor.WHITE)) { // player interacts with bone meal in hand
                BlockType blockType = ItemTable.instance().getBlock(target.getType());
                if (blockType instanceof IBlockGrowable) {
                    // spawn some green particles
                    target.getWorld().showParticle(target.getLocation().add(0.5D, 0.5D, 0.5D),
                            Particle.VILLAGER_HAPPY,
                            0.25F, 0.25F, 0.25F, 0.4F, 12);

                    ((IBlockGrowable) blockType).fertilize(target);

                    // deduct from stack if not in creative mode
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        holding.setAmount(holding.getAmount() - 1);
                    }
                }
            }
        }
    }
}
