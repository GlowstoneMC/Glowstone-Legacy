package net.glowstone.entity.passive;

import com.flowpowered.networking.Message;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;

import java.util.List;

public class GlowOcelot extends GlowTameable implements Ocelot {

    private boolean isSitting = false;
    private Type ocelotType = Type.WILD_OCELOT;

    public GlowOcelot(Location location) {
        this(location, null);
    }

    protected GlowOcelot(Location location, AnimalTamer owner) {
        super(location, EntityType.OCELOT, owner);
    }

    @Override
    public Type getCatType() {
        return ocelotType;
    }

    @Override
    public void setCatType(Type type) {
        this.ocelotType = type;
    }

    @Override
    public boolean isSitting() {
        return isSitting;
    }

    @Override
    public void setSitting(boolean sitting) {
        this.isSitting = sitting;
    }

    @Override
    public List<Message> createSpawnMessage() {
        List<Message> messages = super.createSpawnMessage();
        MetadataMap map = new MetadataMap(GlowOcelot.class);
        map.set(MetadataIndex.OCELOT_TYPE, (byte) this.getCatType().ordinal());
        messages.add(new EntityMetadataMessage(id, map.getEntryList()));
        return messages;
    }
}
