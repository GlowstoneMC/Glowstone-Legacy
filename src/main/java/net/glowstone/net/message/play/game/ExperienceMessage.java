package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class ExperienceMessage implements Message {

    private final float barValue;
    private final int level, totalExp;

}
