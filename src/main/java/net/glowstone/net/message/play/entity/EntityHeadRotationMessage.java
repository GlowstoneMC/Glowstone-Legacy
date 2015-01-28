package net.glowstone.net.message.play.entity;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class EntityHeadRotationMessage implements Message {

    private final int id;
    private final int rotation;

}

