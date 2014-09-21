package net.glowstone.block.state;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import org.bukkit.BannerPattern;
import org.bukkit.DyeColor;
import org.bukkit.block.Banner;

public class GlowBanner extends GlowBlockState implements Banner {

    public GlowBanner(GlowBlock block) {
        super(block);
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
}
