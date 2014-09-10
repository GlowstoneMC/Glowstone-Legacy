package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.inventory.ToolType;
import org.bukkit.Material;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.GlowAnvilInventory;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;

public class BlockAnvil extends BlockFalling {
    public BlockAnvil() {
        super(Material.ANVIL);
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        return player.openInventory(new GlowAnvilInventory(player)) != null;
    }
    
    @Override
    protected void transformToFallingEntity(GlowBlock me) {
      me.setType(Material.AIR);
      me.getWorld().spawnFallingBlock(me.getLocation(), Material.ANVIL, me.getData());
    }    
    
    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        if (tool != null && ToolType.PICKAXE.matches(tool.getType())) {
            ItemStack drop = new ItemStack(Material.ANVIL, 1, (short) (block.getData() / 4));
            return Arrays.asList(drop);
        }
    }
}
