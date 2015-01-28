package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class ClientSettingsMessage implements Message {

    private final String locale;
    private final int viewDistance, chatFlags;
    private final boolean chatColors;
    private final int skinFlags;

}
