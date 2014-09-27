package net.glowstone.network.message.status;

import com.flowpowered.networking.AsyncableMessage;

public final class StatusRequestMessage implements AsyncableMessage {

    @Override
    public boolean isAsync() {
        return true;
    }

}
