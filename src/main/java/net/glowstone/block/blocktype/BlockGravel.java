package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class BlockGravel extends BlockFalling {
    private final Random random = new Random();

    public BlockGravel() {
        super(Material.GRAVEL);
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(random.nextInt(10) == 1 ? Material.FLINT : Material.GRAVEL, 1)));
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        updatePhysics(block);
    }

    @Override
    public void onNearBlockChanges(GlowBlock me, BlockFace position, GlowBlock other, Material oldType, byte oldData, Material newType, byte newData) {
        if (position == BlockFace.DOWN)
            updatePhysics(me);
    }

    @Override
    public void updatePhysics(GlowBlock me) {
        Block below = me.getRelative(BlockFace.DOWN);
        if (below.getType() == Material.AIR) {
            transformToFallingEntity(me);
        }
    }

    private void transformToFallingEntity(GlowBlock me) {
        me.setType(Material.AIR);
        me.getWorld().spawnFallingBlock(me.getLocation(), Material.GRAVEL, (byte) 0);
    }
}
