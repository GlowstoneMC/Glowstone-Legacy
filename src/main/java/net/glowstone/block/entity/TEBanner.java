package net.glowstone.block.entity;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.state.GlowBanner;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.util.nbt.CompoundTag;

public class TEBanner extends TileEntity {

    public TEBanner(GlowBlock block) {
        super(block);
        setSaveId("Banner");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
    }

    @Override
    public GlowBlockState getState() {
        return new GlowBanner(block);
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);
    }
}
