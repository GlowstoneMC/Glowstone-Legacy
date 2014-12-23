package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.StoneType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stone;

import java.util.Arrays;
import java.util.Collection;

public class BlockStone extends DefaultBlockType {

    public BlockStone() {
        super(
                new BlockNeedsTool(ToolType.PICKAXE) {
                    @Override
                    protected Collection<ItemStack> getMinedDrops(GlowBlock block, ItemStack tool) {
                        MaterialData data = block.getState().getData();
                        if (!(data instanceof Stone)) {
                            warnMaterialData(Stone.class, data);
                            return BlockDropless.EMPTY_STACK;
                        }

                        if (((Stone) data).getType() == StoneType.NORMAL) {
                            return Arrays.asList(new ItemStack(Material.COBBLESTONE));
                        } else {
                            return Arrays.asList(new ItemStack(Material.STONE, 1, block.getData()));
                        }
                    }
                }
        );
    }
}
