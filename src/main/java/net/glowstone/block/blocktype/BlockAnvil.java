package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.GlowAnvilInventory;
import net.glowstone.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;

public class BlockAnvil extends DefaultBlockType {

    public BlockAnvil() {
        super(
                new BlockFalling(Material.ANVIL),
                new BlockNeedsTool(ToolType.PICKAXE) {
                    @Override
                    protected Collection<ItemStack> getMinedDrops(GlowBlock block, ItemStack tool) {
                        ItemStack drop = new ItemStack(Material.ANVIL, 1, (short) (block.getData() / 4));
                        return Arrays.asList(drop);
                    }
                }
        );
    }

    @Override
    public Boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        return player.openInventory(new GlowAnvilInventory(player)) != null;
    }
}
