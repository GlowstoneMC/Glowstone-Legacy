package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemPlaceAs extends AbstractItemType {
    private final BlockType placeAs;

    public ItemPlaceAs(Material placeAs) {
        this.placeAs = ItemTable.instance().getBlock(placeAs);
        if (placeAs == null) {
            throw new IllegalArgumentException("Material " + placeAs + " is not a valid block");
        }
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        if (placeAs != null) {
            placeAs.rightClickBlock(player, target, face, holding, clickedLoc);
        }
    }
}
