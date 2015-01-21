package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.inventory.MaterialMatcher;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public abstract class BlockNeedsTool extends AbstractBlockType {
    private final MaterialMatcher neededTool;

    public BlockNeedsTool(MaterialMatcher neededTool) {
        this.neededTool = neededTool;
    }

    @Override
    public final Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        if (neededTool != null &&
                (tool == null || !neededTool.matches(tool.getType())))
            return BlockDropless.EMPTY_STACK;

        return getMinedDrops(block, tool);
    }

    protected abstract Collection<ItemStack> getMinedDrops(GlowBlock block, ItemStack tool);
}
