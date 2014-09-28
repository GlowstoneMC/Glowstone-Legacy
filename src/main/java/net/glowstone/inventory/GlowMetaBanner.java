package net.glowstone.inventory;

import com.google.common.collect.ImmutableMap;
import net.glowstone.block.blocktype.BlockBanner;
import net.glowstone.util.nbt.CompoundTag;
import org.apache.commons.lang.Validate;
import org.bukkit.BannerPattern;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlowMetaBanner extends GlowMetaItem implements BannerMeta {

    private BannerPattern pattern = BannerPattern.builder().build();

    public GlowMetaBanner(GlowMetaItem meta) {
        super(meta);
        if (meta == null || !(meta instanceof GlowMetaBanner)) {
            return;
        }
        GlowMetaBanner banner = (GlowMetaBanner) meta;
        this.pattern = banner.pattern;
    }

    @Override
    public void setPattern(BannerPattern pattern) throws NullPointerException {
        Validate.notNull(pattern, "Pattern cannot be null!");
        this.pattern = pattern;
    }

    @Override
    public BannerPattern getPattern() {
        return pattern;
    }

    @Override
    void writeNbt(CompoundTag tag) {
        super.writeNbt(tag);
        CompoundTag blockEntityTag = new CompoundTag();

        blockEntityTag.putCompoundList("Patterns", BlockBanner.toNBT(pattern));
        tag.putCompound("BlockEntityTag", blockEntityTag);
    }

    @Override
    void readNbt(CompoundTag tag) {
        super.readNbt(tag);
        List<CompoundTag> pattern = tag.getCompound("BlockEntityTag").getCompoundList("Patterns");
        this.pattern = BlockBanner.fromNBT(pattern);
    }

    @Override
    public ItemMeta clone() {
        return new GlowMetaBanner(this);
    }

    @Override
    public boolean isApplicable(Material material) {
        return material == Material.BANNER;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = super.serialize();
        result.put("meta-type", "BANNER");
        List<Map<String, String>> patternsList = new ArrayList<>();
        for(Map.Entry<BannerPattern.Type, DyeColor> layer : pattern.getLayers().entrySet()) {
            patternsList.add(ImmutableMap.of(layer.getKey().toString(), layer.getValue().toString()));
        }
        result.put("pattern", patternsList);
        return result;
    }

}
