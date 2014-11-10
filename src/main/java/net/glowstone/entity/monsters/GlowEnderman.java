package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

public class GlowEnderman extends GlowMonster implements Enderman {

    private MaterialData carriedMaterial;

    public GlowEnderman(Location location) {
        super(location, EntityType.ENDERMAN);
    }

    @Override
    public MaterialData getCarriedMaterial() {
        return carriedMaterial != null ? this.carriedMaterial.clone() : null;
    }

    @Override
    public void setCarriedMaterial(MaterialData materialData) {
        this.carriedMaterial = materialData.clone();
    }
}
