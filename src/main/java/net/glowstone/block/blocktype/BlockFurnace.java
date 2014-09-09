package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TEFurnace;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Furnace;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockFurnace extends BlockContainer {

    public BlockFurnace() {
        setDrops(new ItemStack(Material.FURNACE));
    }

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new TEFurnace(chunk.getBlock(cx, cy, cz));
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);
        final MaterialData data = state.getData();
        if (data instanceof Furnace) {
            ((Furnace) data).setFacingDirection(getOppositeBlockFace(player.getLocation(), false));
            state.setData(data);
        } else {
            GlowServer.logger.warning("Placing " + getMaterial().name() + ": MaterialData was of wrong type (" + data.getClass().getName() + ")");
        }
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        return player.openBlockWindow(block.getLocation(), false, Material.FURNACE, InventoryType.FURNACE) != null;
    }

}
