package net.glowstone.net.message.play.entity;

import net.glowstone.net.flow.Message;
import lombok.Data;
import net.glowstone.entity.meta.MetadataMap;

import java.util.List;

@Data
public final class SpawnMobMessage implements Message {

    private final int id, type, x, y, z, rotation, pitch, headPitch, velX, velY, velZ;
    private final List<MetadataMap.Entry> metadata;

}
