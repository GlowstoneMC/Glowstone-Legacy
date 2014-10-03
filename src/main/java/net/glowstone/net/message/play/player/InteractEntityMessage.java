package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public final class InteractEntityMessage implements Message {

    private final int id, action;
    private final float targetX, targetY, targetZ;

    public InteractEntityMessage(int id, int action) {
        this(id, action, 0, 0, 0);
    }

    public InteractEntityMessage(int id, int action, float targetX, float targetY, float targetZ) {
        this.id = id;
        this.action = action;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public int getId() {
        return id;
    }

    public int getAction() {
        return action;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }

    @Override
    public String toString() {
        return "InteractEntityMessage{" +
                "id=" + id +
                ", action=" + action +
                ", targetX=" + targetX +
                ", targetY=" + targetY +
                ", targetZ=" + targetZ +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InteractEntityMessage that = (InteractEntityMessage) o;

        if (action != that.action) return false;
        if (id != that.id) return false;
        if (Float.compare(that.targetX, targetX) != 0) return false;
        if (Float.compare(that.targetY, targetY) != 0) return false;
        if (Float.compare(that.targetZ, targetZ) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + action;
        result = 31 * result + (targetX != +0.0f ? Float.floatToIntBits(targetX) : 0);
        result = 31 * result + (targetY != +0.0f ? Float.floatToIntBits(targetY) : 0);
        result = 31 * result + (targetZ != +0.0f ? Float.floatToIntBits(targetZ) : 0);
        return result;
    }


    public enum Action {
        INTERACT,
        ATTACK,
        ATTACK_AT
    }
}

