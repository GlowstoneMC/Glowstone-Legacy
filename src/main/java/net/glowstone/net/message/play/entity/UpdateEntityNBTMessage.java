package net.glowstone.net.message.play.entity;

import net.glowstone.net.flow.Message;
import lombok.Data;
import net.glowstone.util.nbt.CompoundTag;

@Data
public final class UpdateEntityNBTMessage implements Message {

    private final int entityId;
    private final CompoundTag tag;

}
