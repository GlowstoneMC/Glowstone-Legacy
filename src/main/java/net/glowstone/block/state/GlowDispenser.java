package net.glowstone.block.state;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockProjectileSource;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TEDispenser;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

public class GlowDispenser extends GlowBlockState implements Dispenser {

    public GlowDispenser(GlowBlock block) {
        super(block);
    }

    private TEDispenser getTileEntity() {
        return (TEDispenser) getBlock().getTileEntity();
    }

    @Override
    public BlockProjectileSource getBlockProjectileSource() {
        return new GlowBlockProjectileSource(this);
    }

    @Override
    public boolean dispense() {
        //TODO: Dispense item
        return false;
    }

    @Override
    public Inventory getInventory() {
        return getTileEntity().getInventory();
    }
}
