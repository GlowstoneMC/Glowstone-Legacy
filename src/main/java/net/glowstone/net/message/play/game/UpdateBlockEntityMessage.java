package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;
import net.glowstone.util.nbt.CompoundTag;

@Data
public final class UpdateBlockEntityMessage implements Message {

    private final int x, y, z, action;
    private final CompoundTag nbt;

}
