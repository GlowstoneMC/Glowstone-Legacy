package net.glowstone.inventory;

import static net.glowstone.inventory.GlowItemFactory.LEATHER_COLOR;

import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.TagType;
import org.bukkit.Material;
import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;


import java.util.Map;

class GlowMetaLeatherArmor extends GlowMetaItem implements LeatherArmorMeta {
    private Color color = LEATHER_COLOR;

    public GlowMetaLeatherArmor (GlowMetaItem meta) {
        super(meta);
        if (meta == null || !(meta instanceof GlowMetaLeatherArmor)) {
            return;
        }
        GlowMetaLeatherArmor leatherArmor = (GlowMetaLeatherArmor)meta;
        this.color = leatherArmor.color;
    }

    public Color getColor () {
        return color;
    }

    public void setColor (Color color) {
        this.color = color;
    }

    public boolean hasColor () {
        return !LEATHER_COLOR.equals(color);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internal stuff

    public GlowMetaLeatherArmor clone () {
        return new GlowMetaLeatherArmor(this);
    }

    public boolean isApplicable(Material material) {
        return (material == Material.LEATHER_BOOTS || material == Material.LEATHER_CHESTPLATE || material == Material.LEATHER_HELMET || material == Material.LEATHER_LEGGINGS);
    }

    void writeNbt (CompoundTag tag) {
        super.writeNbt(tag);
        if (hasColor()) {
            CompoundTag display;
            if (tag.isCompound("display")) {
                display = tag.getCompound("display");
            }
            else {
                display = new CompoundTag();
                tag.putCompound("display", display);
            }
            display.putInt("color", color.asRGB());
        }
    }

    void readNbt (CompoundTag tag) {
        super.readNbt(tag);
        if (tag.isCompound("display")) {
            CompoundTag display = tag.getCompound("display");
            if (display.isInt("color")) {
                this.color = Color.fromRGB(display.getInt("color"));
            }
        }
    }

    public Map<String, Object> serialize () {
        Map<String, Object> result = super.serialize();
        if (hasColor()) {
            result.put("color", color);
        }
        return result;
    }


}
