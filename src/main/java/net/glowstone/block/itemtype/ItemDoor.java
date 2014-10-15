package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemDoor extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        BlockType placeAs = null;

        switch (holding.getType()) {
        case WOOD_DOOR:
            placeAs = ItemTable.instance().getBlock(Material.WOODEN_DOOR);
            break;
        case IRON_DOOR:
            placeAs = ItemTable.instance().getBlock(Material.IRON_DOOR_BLOCK);
            break;
        case SPRUCE_DOOR_ITEM:
            placeAs = ItemTable.instance().getBlock(Material.SPRUCE_DOOR);
            break;
        case BIRCH_DOOR_ITEM:
            placeAs = ItemTable.instance().getBlock(Material.BIRCH_DOOR);
            break;
        case JUNGLE_DOOR_ITEM:
            placeAs = ItemTable.instance().getBlock(Material.JUNGLE_DOOR);
            break;
        case ACACIA_DOOR_ITEM:
            placeAs = ItemTable.instance().getBlock(Material.ACACIA_DOOR);
            break;
        case DARK_OAK_DOOR_ITEM:
            placeAs = ItemTable.instance().getBlock(Material.DARK_OAK_DOOR);
            break;
        default:
            throw new IllegalArgumentException("There is no block for type " + holding.getType() + ", this is a bug.");
        }

        if (placeAs != null)
            placeAs.rightClickBlock(player, target, face, holding, clickedLoc);
    }

}
