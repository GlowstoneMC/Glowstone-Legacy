package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Snowman;
import org.bukkit.inventory.ItemStack;

public class BlockPumpkin extends BlockType {
    private final Random random = new Random();

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.PUMPKIN_SEEDS, random.nextInt(4))));
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        super.afterPlace(player, block, holding);
        if (block.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.SNOW_BLOCK)) {
            if (block.getLocation().add(0, -2, 0).getBlock().getType().equals(Material.SNOW_BLOCK)) {
                Location snowmanLocation = block.getLocation().add(0, -2, 0);
                block.getWorld().spawn(snowmanLocation, Snowman.class);
                block.setType(Material.AIR);
                block.getLocation().add(0, -1, 0).getBlock().setType(Material.AIR);
                block.getLocation().add(0, -2, 0).getBlock().setType(Material.AIR);
            }
        }
    }
}
