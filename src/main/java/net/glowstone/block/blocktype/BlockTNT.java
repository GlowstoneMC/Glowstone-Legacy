package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class BlockTNT extends BlockType {
    public void explodeTNTBlock(GlowBlock tntBlock) {
        tntBlock.setType(Material.AIR);
        World world = tntBlock.getWorld();
        world.spawnEntity(tntBlock.getLocation(), EntityType.PRIMED_TNT);
        //world.playSound(tntBlock.getLocation(), Sound.TNT_PRIMED, 1, 1);
    }
}
