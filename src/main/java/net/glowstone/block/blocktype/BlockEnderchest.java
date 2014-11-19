package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class BlockEnderchest extends DefaultBlockType {

    public BlockEnderchest() {
        super(
                new BlockDirectional(),
                new BlockDirectDrops(Material.OBSIDIAN, 0, 8, ToolType.PICKAXE)
        );
    }

    @Override
    public Boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        // todo: animation?
        player.openInventory(player.getEnderChest());
        return true;
    }
}
