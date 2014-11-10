package net.glowstone.entity.passive;

import com.flowpowered.networking.Message;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

import java.util.List;

public class GlowWolf extends GlowTameable implements Wolf {

    private boolean angry = false;

    private boolean sitting = false;

    private DyeColor collarColor = DyeColor.RED;

    public GlowWolf(Location location) {
        this(location, null);
    }

    protected GlowWolf(Location location, AnimalTamer owner) {
        super(location, EntityType.WOLF, owner);
    }

    @Override
    public boolean isAngry() {
        return angry;
    }

    @Override
    public void setAngry(boolean angry) {
        this.angry = angry;
    }

    @Override
    public boolean isSitting() {
        return sitting;
    }

    @Override
    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    @Override
    public DyeColor getCollarColor() {
        return collarColor;
    }

    @Override
    public void setCollarColor(DyeColor dyeColor) {
        this.collarColor = dyeColor;
    }

    @Override
    public List<Message> createSpawnMessage() {
        List<Message> messages = super.createSpawnMessage();
        MetadataMap map = new MetadataMap(GlowWolf.class);

        map.set(MetadataIndex.WOLF_COLOR, getColorByte());
        messages.add(new EntityMetadataMessage(id, map.getEntryList()));
        return messages;
    }


    private byte getColorByte() {
        return (byte) (this.getCollarColor().getData());
    }
}
