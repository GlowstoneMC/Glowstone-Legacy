package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class BlockTNT extends BlockType {
    public void explodeTNTBlock(GlowBlock tntBlock) {
        tntBlock.setType(Material.AIR);
        tntBlock.getWorld().spawnEntity(tntBlock.getLocation(), EntityType.PRIMED_TNT);
    }
}
