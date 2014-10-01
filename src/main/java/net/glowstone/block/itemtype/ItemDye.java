package net.glowstone.block.itemtype;

import java.util.Random;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.Tree;
import org.bukkit.util.Vector;

public class ItemDye extends ItemType {

    // TODO
    // use GlowWorld random instance instead 
    private final Random random = new Random();

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        final Dye dye = (Dye) holding.getData();
        if (dye.getColor().equals(DyeColor.WHITE)) { // player interacts with bone meal in hand
            if (target.getType().equals(Material.SAPLING)) {

                // TODO
                // check there's enough place around the sapling
                // make generateTree to not overwrite blocks
                // handle big trees
                // wait particles branch is merged and uncomment below
                // block.getWorld().showParticle(loc, Particle.HAPPY_VILLAGER, 0, 0, 0, 0.25f, 5);

                final Tree tree = (Tree) target.getState().getData();
                TreeType type = null;
                switch (tree.getSpecies()) {
                    case GENERIC:
                        type = TreeType.TREE;
                        break;
                    case REDWOOD:
                        type = TreeType.REDWOOD;
                        break;
                    case BIRCH:
                        type = TreeType.BIRCH;
                        break;
                    case JUNGLE:
                        type = TreeType.JUNGLE;
                        break;
                    case ACACIA:
                        type = TreeType.ACACIA;
                        break;
                    case DARK_OAK:
                        type = TreeType.DARK_OAK;
                        break;
                    default:
                        type = TreeType.TREE;
                }

                // deduct from stack if not in creative mode
                if (player.getGameMode() != GameMode.CREATIVE) {
                    holding.setAmount(holding.getAmount() - 1);
                }

                if (random.nextInt(6) == 0) {
                    target.getWorld().generateTree(target.getLocation(), type);
                }
            }
        }
    }
}
