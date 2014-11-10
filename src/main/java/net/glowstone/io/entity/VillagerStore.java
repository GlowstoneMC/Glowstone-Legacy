package net.glowstone.io.entity;

import net.glowstone.entity.passive.GlowVillager;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.entity.Villager;

public class VillagerStore extends AgeableStore<GlowVillager> {

    public VillagerStore() {
        super(GlowVillager.class, "Villager");
    }

    @Override
    public void load(GlowVillager entity, CompoundTag compound) {
        super.load(entity, compound);
        if (compound.containsKey("Profession")) {
            entity.setProfession(Villager.Profession.getProfession(compound.getInt("Profession")));
        }
    }

    @Override
    public void save(GlowVillager entity, CompoundTag tag) {
        super.save(entity, tag);
        if (entity.getProfession() != null) {
            tag.putInt("Profession", entity.getProfession().getId());
        }
        // TODO tag.putInt("Riches", );
        // TODO tag.putInt("Career", );
        // TODO tag.putint("CareerLevel", );
        // TODO tag.putBool("Willing", );
        /*
        if (entity.getTradeOfferList() != null && !entity.getTradeOfferList().isEmpty()) {
           // TODO write tradeoffer list
        }
        // TODO write inventory stuff
         */
    }
}
