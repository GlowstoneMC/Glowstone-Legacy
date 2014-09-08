package net.glowstone.block.blocktype;

import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
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
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        // todo: animation?
        player.openInventory(player.getEnderChest());
        return true;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        MaterialData data = state.getData();
        if (data instanceof EnderChest) {
            ((EnderChest)data).setFacingDirection(getOppositeDirection(player));
            state.setData(data);
        } else {
            // complain?
            GlowServer.logger.warning("Placing EnderChest: MaterialData was of wrong type");
        }
    }
    
    public BlockFace getOppositeDirection(GlowPlayer player) {
        double rot = player.getLocation().getYaw() % 360.0F;

        if (rot < 0.0D) {
            rot += 360.0D;
        }

        if ((0.0D <= rot) && (rot < 45.0D))
            return BlockFace.NORTH;
        if ((45.0D <= rot) && (rot < 135.0D))
            return BlockFace.EAST;
        if ((135.0D <= rot) && (rot < 225.0D))
            return BlockFace.SOUTH;
        if ((225.0D <= rot) && (rot < 315.0D))
            return BlockFace.WEST;
        if ((315.0D <= rot) && (rot < 360.0D)) {
            return BlockFace.NORTH;
        }
        return BlockFace.EAST;
    }
}
