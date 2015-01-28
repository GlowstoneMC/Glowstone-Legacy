package net.glowstone.net.message.play.entity;

import net.glowstone.net.flow.Message;
import lombok.Data;

import java.util.List;

@Data
public final class DestroyEntitiesMessage implements Message {

    private final List<Integer> ids;

}
