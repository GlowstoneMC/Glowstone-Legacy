package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class PlayEffectMessage implements Message {

    private final int id;
    private final int x, y, z;
    private final int data;
    private final boolean ignoreDistance;

}
