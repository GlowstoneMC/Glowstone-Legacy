package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;

public final class AnimateEntityMessage implements Message {

    public static final int IN_SWING_ARM = 1;
    public static final int IN_HURT = 2;
    public static final int IN_LEAVE_BED = 3;

    public static final int OUT_SWING_ARM = 0;
    public static final int OUT_HURT = 1;
    public static final int OUT_LEAVE_BED = 2;

    private final int id, animation;

    public AnimateEntityMessage(int id, int animation) {
        this.id = id;
        this.animation = animation;
    }

    public int getId() {
        return id;
    }

    public int getAnimation() {
        return animation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimateEntityMessage that = (AnimateEntityMessage) o;

        if (animation != that.animation) return false;
        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + animation;
        return result;
    }

    @Override
    public String toString() {
        return "AnimateEntityMessage{id=" + id + ",animation=" + animation + "}";
    }
}
