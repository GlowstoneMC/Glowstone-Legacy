package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public final class PlayerAbilitiesMessage implements Message {

    private final int flags;
    private final float flySpeed, walkSpeed;

    public PlayerAbilitiesMessage(int flags, float flySpeed, float walkSpeed) {
        this.flags = flags;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    public int getFlags() {
        return flags;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerAbilitiesMessage that = (PlayerAbilitiesMessage) o;

        if (flags != that.flags) return false;
        if (Float.compare(that.flySpeed, flySpeed) != 0) return false;
        if (Float.compare(that.walkSpeed, walkSpeed) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = flags;
        result = 31 * result + (flySpeed != +0.0f ? Float.floatToIntBits(flySpeed) : 0);
        result = 31 * result + (walkSpeed != +0.0f ? Float.floatToIntBits(walkSpeed) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlayerAbilitiesMessage{" +
                "flags=" + flags +
                ", flySpeed=" + flySpeed +
                ", walkSpeed=" + walkSpeed +
                '}';
    }
}

