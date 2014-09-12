package net.glowstone.net.message.status;

import com.flowpowered.networking.AsyncableMessage;

public final class StatusRequestMessage implements AsyncableMessage {

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public boolean equals(Object message) {
        if (message instanceof StatusRequestMessage) {
            return true;
        }
        return false;
    }
}
