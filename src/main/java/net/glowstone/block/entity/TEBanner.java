package net.glowstone.block.entity;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.blocktype.BlockBanner;
import net.glowstone.block.state.GlowBanner;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.BannerPattern;
import org.bukkit.DyeColor;

import java.util.List;

public class TEBanner extends TileEntity {

    private DyeColor base;
    private BannerPattern pattern;

    public TEBanner(GlowBlock block) {
        super(block);
        setSaveId("Banner");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        List<CompoundTag> pattern = tag.getCompoundList("Patterns");
        this.pattern = BlockBanner.fromNBT(pattern);
        this.base = DyeColor.getByDyeData(tag.getByte("Base"));
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putCompoundList("Patterns", BlockBanner.toNBT(pattern));
        tag.putByte("Base", base.getDyeData());
    }

    @Override
    public GlowBlockState getState() {
        return new GlowBanner(block);
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);
        CompoundTag nbt = new CompoundTag();
        saveNbt(nbt);
        player.sendBannerChange(getBlock().getLocation(), nbt);
    }

    public void setPattern(BannerPattern pattern) {
        this.pattern = pattern;
    }

    public void setBase(DyeColor base) {
        this.base = base;
    }

    public DyeColor getBase() {
        return base;
    }

    public BannerPattern getPattern() {
        return pattern;
    }
}
