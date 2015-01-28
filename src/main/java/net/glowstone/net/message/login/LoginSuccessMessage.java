package net.glowstone.net.message.login;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class LoginSuccessMessage implements Message {

    private final String uuid;
    private final String username;

}
