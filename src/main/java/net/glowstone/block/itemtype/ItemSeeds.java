package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.util.Vector;

public class ItemSeeds extends ItemType {
    private Material cropsType;
    private Material soilType;

    public ItemSeeds(Material cropsType, Material soilType) {
        this.cropsType = cropsType;
        this.soilType = soilType;
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        if (target.getType().equals(soilType)
                && target.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
            final GlowBlockState state = target.getRelative(BlockFace.UP).getState();
            state.setType(cropsType);
            state.setData(new Crops(CropState.SEEDED));
            state.update(true);

            // deduct from stack if not in creative mode
            if (player.getGameMode() != GameMode.CREATIVE) {
                holding.setAmount(holding.getAmount() - 1);
            }
        }
    }
}
