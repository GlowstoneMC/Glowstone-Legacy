package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;

import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class BlockFire extends BlockType {

    public boolean canAbsorb(GlowBlock block, BlockFace face, ItemStack item) {
        return true;
    }
}
