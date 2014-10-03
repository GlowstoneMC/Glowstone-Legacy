package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;

public final class RelativeEntityPositionMessage implements Message {

    private final int id, deltaX, deltaY, deltaZ;
    private final boolean onGround;

    public RelativeEntityPositionMessage(int id, int deltaX, int deltaY, int deltaZ) {
        this(id, deltaX, deltaY, deltaZ, true);
    }

    public RelativeEntityPositionMessage(int id, int deltaX, int deltaY, int deltaZ, boolean onGround) {
        this.id = id;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.onGround = onGround;
    }

    public int getId() {
        return id;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public int getDeltaZ() {
        return deltaZ;
    }

    public boolean getOnGround() {
        return onGround;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelativeEntityPositionMessage that = (RelativeEntityPositionMessage) o;

        if (deltaX != that.deltaX) return false;
        if (deltaY != that.deltaY) return false;
        if (deltaZ != that.deltaZ) return false;
        if (id != that.id) return false;
        if (onGround != that.onGround) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + deltaX;
        result = 31 * result + deltaY;
        result = 31 * result + deltaZ;
        result = 31 * result + (onGround ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelativeEntityPositionMessage{" +
                "id=" + id +
                ", deltaX=" + deltaX +
                ", deltaY=" + deltaY +
                ", deltaZ=" + deltaZ +
                ", onGround=" + onGround +
                '}';
    }

}
