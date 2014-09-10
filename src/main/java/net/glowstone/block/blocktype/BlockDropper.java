package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.GlowChunk;
import net.glowstone.block.entity.TEDropper;
import net.glowstone.block.entity.TileEntity;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.util.Vector;

public class BlockDropper extends BlockDispenser {

    public BlockDropper() {
        setDrops(new ItemStack(Material.DROPPER));
    }

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new TEDropper(chunk.getBlock(cx, cy, cz));
    }
    
    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        return player.openBlockWindow(block.getLocation(), false, Material.DROPPER, InventoryType.DROPPER) != null;
    }

}
