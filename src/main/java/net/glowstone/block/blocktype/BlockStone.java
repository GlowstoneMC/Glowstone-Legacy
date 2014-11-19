package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class BlockStone extends DefaultBlockType {

    public BlockStone() {
        super(
                new BlockNeedsTool(ToolType.PICKAXE) {
                    @Override
                    protected Collection<ItemStack> getMinedDrops(GlowBlock block, ItemStack tool) {
                        //TODO: Use MaterialData instead of magic value
                        if (block.getData() == 0) {
                            return Arrays.asList(new ItemStack(Material.COBBLESTONE));
                        } else {
                            return Arrays.asList(new ItemStack(Material.STONE, 1, block.getData()));
                        }
                    }
                }
        );
    }
}
