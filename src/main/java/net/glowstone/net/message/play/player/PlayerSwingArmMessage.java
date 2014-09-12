package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public final class PlayerSwingArmMessage implements Message {

    @Override
    public String toString() {
        return "PlayerSwingArmMessage";
    }

    @Override
    public boolean equals(Object message) {
        if (message instanceof PlayerSwingArmMessage) {
            return true;
        }
        return false;
    }

}

