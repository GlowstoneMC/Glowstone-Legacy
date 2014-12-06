package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class BlockFire extends BlockNeedsAttached {

    @Override
    public boolean canOverride(GlowBlock block, BlockFace face, ItemStack holding) {
        return true;
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        return BlockDropless.EMPTY_STACK;
    }
}
