package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TEBanner;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;

public class BlockBanner extends BlockType {

    public BlockBanner() {
        setDrops(new ItemStack(Material.BANNER));
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        //TODO: Drops
        return super.getDrops(block);
    }

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new TEBanner(chunk.getBlock(cx, cy, cz));
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        //TODO: Place block
        super.placeBlock(player, state, face, holding, clickedLoc);
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        //TODO: TileEntity data
        super.afterPlace(player, block, holding);
    }
}
