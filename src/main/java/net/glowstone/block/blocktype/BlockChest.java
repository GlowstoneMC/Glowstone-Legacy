package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TEChest;
import net.glowstone.block.entity.TEContainer;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.block.state.GlowChest;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.block.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Chest;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockChest extends BlockType {

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new TEChest(chunk.getBlock(cx, cy, cz));
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        MaterialData data = state.getData();
        if (data instanceof Chest) {
            // todo: determine facing direction
            ((Chest) data).setFacingDirection(BlockFace.EAST);
            state.setData(data);
        } else {
            // complain?
            GlowServer.logger.warning("Placing Chest: MaterialData was of wrong type");
        }
    }
    
    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
	TileEntity te = block.getTileEntity();
	if (te instanceof TEContainer) {
		player.openInventory(((TEContainer) te).getInventory());
		//Only open when chest is closed player interacts, only close when player closes chest inventory
		if (((GlowChest) block.getState()).getState() == 0){
			player.SetBindChest(block.getLocation());
			((GlowChest) block.getState()).setState((byte)1);
			((GlowChest) block.getState()).ChestAnimation((byte)1);
		}
		return true;
	}
	return false;
	}
    
}
