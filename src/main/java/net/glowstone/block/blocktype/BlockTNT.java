package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class BlockTNT extends BlockType {
    public void explodeTNTBlock(GlowBlock tntBlock) {
        tntBlock.setType(Material.AIR);
        World world = tntBlock.getWorld();
        world.spawnEntity(tntBlock.getLocation(), EntityType.PRIMED_TNT);
        world.playSound(tntBlock.getLocation(), Sound.FUSE, 1, 1);
    }
}
