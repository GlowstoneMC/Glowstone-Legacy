package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Base interface for specific types of items.
 */
public interface ItemType {
    /**
     * Called when this ItemType is initialised.
     * @param base The base of this ItemType if any, or null
     * @param id The id of the base ItemType
     */
    void onInit(ItemType base, int id);

    int getId();

    Material getMaterial();

    /**
     * Called when a player right-clicks in midair while holding this item.
     * Also called by default if rightClickBlock is not overridden.
     * @param player The player
     * @param holding The ItemStack the player was holding
     */
    void rightClickAir(GlowPlayer player, ItemStack holding);

    /**
     * Called when a player right-clicks on a block while holding this item.
     * @param player The player
     * @param target The block the player right-clicked
     * @param face The face on which the click occurred
     * @param holding The ItemStack the player was holding
     * @param clickedLoc The coordinates at which the click occurred
     */
    void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc);
}
