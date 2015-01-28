package net.glowstone.net.message.play.inv;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class HeldItemMessage implements Message {

    private final int slot;

}

