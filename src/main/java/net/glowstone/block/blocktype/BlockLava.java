package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlockState;
import org.bukkit.Material;

public class BlockLava extends BlockLiquid {

    public BlockLava () {
        this.setBucketType(Material.LAVA_BUCKET);
    }

    public boolean isCollectible (GlowBlockState target) {
        return target.getType() == Material.STATIONARY_LAVA || (target.getType() == Material.LAVA && target.getRawData() == 8);
    }

}
