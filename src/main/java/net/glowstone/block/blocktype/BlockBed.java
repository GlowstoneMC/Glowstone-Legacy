package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.material.MaterialData;

public class BlockBed extends DefaultBlockType {

    public BlockBed() {
        super(
                new BlockDropWithoutData(),
                new BlockDirectional()
        );
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        super.afterPlace(player, block, holding);

        if (block.getType() == Material.BED_BLOCK) {
            BlockFace direction = ((Bed) block.getState().getData()).getFacing();
            GlowBlock headBlock = block.getRelative(direction);
            headBlock.setType(Material.BED_BLOCK);
            GlowBlockState headBlockState = headBlock.getState();
            MaterialData data = headBlockState.getData();
            ((Bed) data).setHeadOfBed(true);
            ((Bed) data).setFacingDirection(direction);
            headBlockState.setData(data);
            headBlockState.update(true);
        }
    }
}
