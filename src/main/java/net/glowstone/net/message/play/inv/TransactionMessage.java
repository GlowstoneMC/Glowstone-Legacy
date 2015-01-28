package net.glowstone.net.message.play.inv;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class TransactionMessage implements Message {

    private final int id, transaction;
    private final boolean accepted;

}
