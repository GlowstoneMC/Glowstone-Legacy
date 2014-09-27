package net.glowstone.network.message.play.game;

import com.flowpowered.networking.Message;

public final class SignEditorMessage implements Message {

    private final int x, y, z;

    public SignEditorMessage(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "SignEditorMessage{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

