package net.glowstone.entity.passive;

import net.glowstone.entity.GlowAgeable;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.Random;

public class GlowVillager extends GlowAgeable implements Villager {

    private Profession profession;

    public GlowVillager(Location location) {
        super(location, EntityType.VILLAGER);
        this.profession = Profession.values()[new Random().nextInt(4)];
    }

    @Override
    public Profession getProfession() {
        return profession;
    }

    @Override
    public void setProfession(Profession profession) {
        this.profession = profession;
    }
}
