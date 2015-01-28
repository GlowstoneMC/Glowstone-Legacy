package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class TimeMessage implements Message {

    private final long worldAge, time;

}
