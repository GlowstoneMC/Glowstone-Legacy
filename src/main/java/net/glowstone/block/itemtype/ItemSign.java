package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemSign extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        if (face == BlockFace.UP) {
            setPlaceAs(ItemTable.instance().getBlock(Material.SIGN_POST));
        } else if (face == BlockFace.DOWN) {
            return;
        } else {
        	setPlaceAs(ItemTable.instance().getBlock(Material.WALL_SIGN));
        }
        getPlaceAs().rightClickBlock(player, target, face, holding, clickedLoc);
    }

}
