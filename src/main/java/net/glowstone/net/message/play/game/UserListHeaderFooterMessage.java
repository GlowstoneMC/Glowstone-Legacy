package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;
import net.glowstone.util.TextMessage;

@Data
public final class UserListHeaderFooterMessage implements Message {

    private final TextMessage header, footer;

}
