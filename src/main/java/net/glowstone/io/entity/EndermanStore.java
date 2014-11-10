package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowEnderman;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class EndermanStore extends MonsterStore<GlowEnderman> {

    public EndermanStore() {
        super(GlowEnderman.class, "Enderman");
    }

    @Override
    public void load(GlowEnderman entity, CompoundTag compound) {
        super.load(entity, compound);
        if (compound.containsKey("carried")) { // TODO possibly make this cleaner
            Material material;
            MaterialData materialData;
            short id = compound.getShort("carried");
            short carriedId = compound.getShort("carriedData");
            material = Material.getMaterial(id);
            if (material != null) {
                materialData = new MaterialData(material, (byte) carriedId);
                entity.setCarriedMaterial(materialData);
            }
        }
    }

    @Override
    public void save(GlowEnderman entity, CompoundTag tag) {
        super.save(entity, tag);
        if (entity.getCarriedMaterial() != null) {
            tag.putShort("carried", entity.getCarriedMaterial().getItemTypeId());
            tag.putShort("carriedData", entity.getCarriedMaterial().getData());
        }
    }
}
