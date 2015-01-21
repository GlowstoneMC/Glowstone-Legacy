package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.inventory.MaterialMatcher;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class BlockDropWithoutData extends BlockNeedsTool {
    public BlockDropWithoutData(MaterialMatcher neededTool) {
        super(neededTool);
    }

    public BlockDropWithoutData() {
        super(null);
    }

    @Override
    protected Collection<ItemStack> getMinedDrops(GlowBlock block, ItemStack tool) {
        return Arrays.asList(new ItemStack(block.getType()));
    }
}
