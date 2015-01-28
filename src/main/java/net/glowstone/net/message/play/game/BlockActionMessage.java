package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class BlockActionMessage implements Message {

    private final int x, y, z, data1, data2, blockType;

}
