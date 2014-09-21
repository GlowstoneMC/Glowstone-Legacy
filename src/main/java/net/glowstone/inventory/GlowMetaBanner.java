package net.glowstone.inventory;

import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.BannerPattern;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.BannerMeta;

public class GlowMetaBanner extends GlowMetaItem implements BannerMeta {

    public GlowMetaBanner(GlowMetaItem meta) {
        super(meta);
    }

    @Override
    public void setBase(DyeColor dyeColor) throws IllegalArgumentException {

    }

    @Override
    public DyeColor getBase() {
        return null;
    }

    @Override
    public void setPattern(BannerPattern bannerPattern) throws IllegalArgumentException {

    }

    @Override
    public BannerPattern getPattern() {
        return null;
    }

    @Override
    void writeNbt(CompoundTag tag) {
        super.writeNbt(tag);
    }

    @Override
    void readNbt(CompoundTag tag) {
        super.readNbt(tag);
    }

}
