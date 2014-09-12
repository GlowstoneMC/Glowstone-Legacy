package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public final class DiggingMessage implements Message {

    public static final int START_DIGGING = 0;
    public static final int FINISH_DIGGING = 2;
    public static final int STATE_DROP_ITEM = 4;

    private final int state, x, y, z, face;

    public DiggingMessage(int state, int x, int y, int z, int face) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
    }

    public int getState() {
        return state;
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

    public int getFace() {
        return face;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiggingMessage that = (DiggingMessage) o;

        if (face != that.face) return false;
        if (state != that.state) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = state;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + face;
        return result;
    }

    @Override
    public String toString() {
        return "DiggingMessage{state=" + state + ",x=" + x + ",y=" + y + ",z=" + z + ",face=" + face + "}";
    }
}
