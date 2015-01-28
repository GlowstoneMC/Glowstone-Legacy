package net.glowstone.net.message;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class SetCompressionMessage implements Message {

    private final int threshold;

}

