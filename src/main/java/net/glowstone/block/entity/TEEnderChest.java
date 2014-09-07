package net.glowstone.block.entity;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.state.GlowEnderChest;

import org.bukkit.event.inventory.InventoryType;

/**
 * Tile entity for Chests.
 */
public class TEEnderChest extends TEContainer {

    public TEEnderChest(GlowBlock block) {
        super(block, InventoryType.ENDER_CHEST);
        setSaveId("EnderChest");
    }

    @Override
    public GlowBlockState getState() {
        return new GlowEnderChest(block);
    }
}
