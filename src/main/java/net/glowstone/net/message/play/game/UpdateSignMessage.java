package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import net.glowstone.util.TextMessage;

import java.util.Arrays;

public final class UpdateSignMessage implements Message {

    private final int x, y, z;
    private final TextMessage[] message;

    public UpdateSignMessage(int x, int y, int z, TextMessage[] message) {
        if (message.length != 4) {
            throw new IllegalArgumentException();
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.message = message;
    }

    public static UpdateSignMessage fromPlainText(int x, int y, int z, String[] message) {
        if (message.length != 4) {
            throw new IllegalArgumentException();
        }

        TextMessage[] encoded = new TextMessage[4];
        for (int i = 0; i < 4; ++i) {
            encoded[i] = new TextMessage(message[i]);
        }
        return new UpdateSignMessage(x, y, z, encoded);
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

    public TextMessage[] getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateSignMessage that = (UpdateSignMessage) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (!Arrays.equals(message, that.message)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (message != null ? Arrays.hashCode(message) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UpdateSignMessage{x=" + x + ",y=" + y + ",z=" + z + ",message=" + Arrays.toString(message) + "}";
    }
}
