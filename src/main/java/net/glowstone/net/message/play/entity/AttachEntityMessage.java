package net.glowstone.net.message.play.entity;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class AttachEntityMessage implements Message {

    private final int id, vehicle;
    private final boolean leash;

}
