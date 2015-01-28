package net.glowstone.net.message.play.entity;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class CollectItemMessage implements Message {

    private final int id, collector;

}
