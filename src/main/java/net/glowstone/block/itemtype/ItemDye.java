package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.block.blocktype.IBlockGrowable;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.CocoaPlant.CocoaPlantSize;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;
import org.bukkit.util.Vector;

public class ItemDye extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        MaterialData data = holding.getData();
        if (data instanceof Dye) {
            final Dye dye = (Dye) data;
            if (dye.getColor().equals(DyeColor.WHITE)) { // player interacts with bone meal in hand
                BlockType blockType = ItemTable.instance().getBlock(target.getType());
                if (blockType instanceof IBlockGrowable) {
                    IBlockGrowable growable = (IBlockGrowable) blockType;
                    if (growable.isFertilizable(target)) {
                        // spawn some green particles
                        target.getWorld().showParticle(target.getLocation().add(0.5D, 0.5D, 0.5D),
                                Particle.VILLAGER_HAPPY, 0.25F, 0.25F, 0.25F, 0.4F, 12);

                        if (growable.canGrowWithChance(target)) {
                            growable.grow(target);
                        }

                        // deduct from stack if not in creative mode
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            holding.setAmount(holding.getAmount() - 1);
                        }
                    }
                }
            } else if (dye.getColor().equals(DyeColor.BROWN)) { // player interacts with cocoa beans in hand
                if (target.getType().equals(Material.LOG)) {
                    data = target.getState().getData();
                    if (data instanceof Tree) {
                        if (((Tree) data).getSpecies().equals(TreeSpecies.JUNGLE)) {
                            if (target.getRelative(face).getType().equals(Material.AIR)) {
                                final GlowBlockState state = target.getRelative(face).getState();
                                state.setType(Material.COCOA);
                                state.setData(new CocoaPlant(CocoaPlantSize.SMALL, face.getOppositeFace()));
                                state.update(true);

                                // deduct from stack if not in creative mode
                                if (player.getGameMode() != GameMode.CREATIVE) {
                                    holding.setAmount(holding.getAmount() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
