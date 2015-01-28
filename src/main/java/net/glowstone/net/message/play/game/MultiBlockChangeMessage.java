package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.Message;
import lombok.Data;

import java.util.List;

@Data
public final class MultiBlockChangeMessage implements Message {

    private final int chunkX, chunkZ;
    private final List<BlockChangeMessage> records;

}
