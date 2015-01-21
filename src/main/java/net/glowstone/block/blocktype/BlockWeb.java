package net.glowstone.block.blocktype;

import net.glowstone.inventory.ToolType;
import org.bukkit.Material;

public class BlockWeb extends DefaultBlockType {

    public BlockWeb() {
        super(
                new AbstractBlockType(),
                new BlockDirectDrops(Material.STRING, ToolType.SHEARS_SWORDS)
        );
    }
}
