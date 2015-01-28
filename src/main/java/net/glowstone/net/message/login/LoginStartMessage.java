package net.glowstone.net.message.login;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class LoginStartMessage implements Message {

    private final String username;

}
