package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemHoe extends ItemTool {

    public ItemHoe(Material material) {
        super(material.getMaxDurability());
    }

    @Override
    protected boolean onToolRightClick(GlowPlayer player, ItemStack tool, GlowBlock target, BlockFace face, Vector clickedLoc) {
        if (target.getRelative(BlockFace.UP).getType() == Material.AIR) {
            if (target.getType() == Material.GRASS || (target.getType() == Material.DIRT && target.getData() == 0)) {
                // grass or ordinary dirt: become soil
                target.getWorld().playSound(target.getLocation().add(0.5D, 0.5D, 0.5D), Sound.STEP_GRAVEL, 1, 0.8F);
                target.setType(Material.SOIL);
                return true;
            } else if (target.getType() == Material.DIRT && target.getData() == 1) {
                // coarse dirt: become regular dirt
                target.getWorld().playSound(target.getLocation().add(0.5D, 0.5D, 0.5D), Sound.STEP_GRAVEL, 1, 0.8F);
                target.setData((byte) 0); // changing it to normal dirt
            }
        }
        return false;
    }

    @Override
    public int calculateBreakDamage(GlowBlock target) {
        return 0;
    }
}
