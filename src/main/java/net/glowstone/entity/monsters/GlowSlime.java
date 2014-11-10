package net.glowstone.entity.monsters;

import com.flowpowered.networking.Message;
import net.glowstone.entity.GlowLivingEntity;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.net.message.play.entity.EntityHeadRotationMessage;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.SpawnMobMessage;
import net.glowstone.util.Position;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GlowSlime extends GlowLivingEntity implements Slime {

    private int size = 0;
    private EntityType type;

    protected GlowSlime(Location location, EntityType type) {
        this(location);
        this.type = type;
    }

    public GlowSlime(Location location) {
        super(location);
        this.type = EntityType.SLIME;
        this.size = new Random().nextInt(4);
    }

    @Override
    public List<Message> createSpawnMessage() {
        List<Message> result = new LinkedList<>();

        // spawn mob
        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);
        int yaw = Position.getIntYaw(location);
        int pitch = Position.getIntPitch(location);
        result.add(new SpawnMobMessage(id, type.getTypeId(), x, y, z, yaw, pitch, pitch, 0, 0, 0, metadata.getEntryList()));

        // head facing
        result.add(new EntityHeadRotationMessage(id, yaw));

        MetadataMap map = new MetadataMap(GlowSlime.class);
        map.set(MetadataIndex.SLIME_SIZE, this.getSize());
        result.add(new EntityMetadataMessage(id, map.getEntryList()));
        return result;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int i) {
        this.size = i;
    }

    @Override
    public EntityType getType() {
        return type;
    }
}
