package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TEContainer;
import net.glowstone.block.entity.TEEnderChest;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.block.state.GlowEnderChest;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.EnderChest;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockEnderchest extends BlockType {
    public BlockEnderchest() {
        setDrops(new ItemStack(Material.OBSIDIAN, 8));
    }
    
    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new TEEnderChest(chunk.getBlock(cx, cy, cz));
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
	//player.openInventory(player.getEnderChest());
	TileEntity te = block.getTileEntity();
	if (te instanceof TEContainer) {
		player.openInventory(player.getEnderChest());
		//Only open when chest is closed player interacts, only close when player closes chest inventory
		if (((GlowEnderChest) block.getState()).getState() == 0){
			block.getWorld().playSound(block.getLocation(), Sound.CHEST_OPEN, 1, 1);
			player.SetBindChest(block.getLocation());
			((GlowEnderChest) block.getState()).setState((byte)1);
			((GlowEnderChest) block.getState()).ChestAnimation((byte)1);
		}
		return true;
	}
	return false;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        MaterialData data = state.getData();
        if (data instanceof EnderChest) {
            // todo: determine facing direction
            ((EnderChest) data).setFacingDirection(BlockFace.EAST);
            state.setData(data);
        } else {
            // complain?
            GlowServer.logger.warning("Placing EnderChest: MaterialData was of wrong type");
        }
    }
}
