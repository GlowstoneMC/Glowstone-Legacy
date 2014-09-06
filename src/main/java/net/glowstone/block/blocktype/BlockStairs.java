package net.glowstone.block.blocktype;

import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;


public class BlockStairs extends BlockType {

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);
        
        MaterialData data = state.getData();
        if (data instanceof Stairs) {
            ((Stairs) data).setFacingDirection(getOppositeDirection(player));
            
            if (face == BlockFace.DOWN || face != BlockFace.UP && clickedLoc.getY() >= 8) {
                ((Stairs) data).setInverted(true);   
            }

            state.setData(data);
        } else {
            // complain?
            GlowServer.logger.warning("Placing Stairs: MaterialData was of wrong type");
        }
    }
    
    public BlockFace getOppositeDirection(GlowPlayer player) {
        double rot = player.getLocation().getYaw() % 360;
        
        if (rot < 0) {
            rot += 360.0;
        }
        
        if (0 <= rot && rot < 45) {
            return BlockFace.SOUTH;
        } else if (45 <= rot && rot < 135) {
            return BlockFace.WEST;
        } else if (135 <= rot && rot < 225) {
            return BlockFace.NORTH;
        } else if (225 <= rot && rot < 315) {
            return BlockFace.EAST;
        } else if (315 <= rot && rot < 360.0) {
            return BlockFace.SOUTH;
        } else {
            return BlockFace.EAST;
        }
    }
}
