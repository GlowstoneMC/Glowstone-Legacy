package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a block that falls down, when there's no block below it.
 */
public class BlockFalling extends BlockType {
    private final Material drop;

    public BlockFalling(Material drop) {
        this.drop = drop;
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        updatePhysics(block);
    }

    @Override
    public void onNearBlockChanged(GlowBlock me, BlockFace position, GlowBlock other, Material oldType, byte oldData, Material newType, byte newData) {
        if (position == BlockFace.DOWN)
            updatePhysics(me);
    }

    @Override
    public void updatePhysics(GlowBlock me) {
        Block below = me.getRelative(BlockFace.DOWN);
        switch (below.getType()) {
            case AIR:
            case FIRE:
            case WATER:
            case LAVA:
                transformToFallingEntity(me);
        }
    }

    protected void transformToFallingEntity(GlowBlock me) {
        me.setType(Material.AIR);
        me.getWorld().spawnFallingBlock(me.getLocation(), drop, (byte) 0);
    }
}
