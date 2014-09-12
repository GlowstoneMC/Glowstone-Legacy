package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public final class SteerVehicleMessage implements Message {

    private final float sideways, forward;
    private final boolean jump, unmount;

    public SteerVehicleMessage(float sideways, float forward, boolean jump, boolean unmount) {
        this.sideways = sideways;
        this.forward = forward;
        this.jump = jump;
        this.unmount = unmount;
    }

    public float getSideways() {
        return sideways;
    }

    public float getForward() {
        return forward;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isUnmount() {
        return unmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SteerVehicleMessage that = (SteerVehicleMessage) o;

        if (Float.compare(that.forward, forward) != 0) return false;
        if (jump != that.jump) return false;
        if (Float.compare(that.sideways, sideways) != 0) return false;
        if (unmount != that.unmount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (sideways != +0.0f ? Float.floatToIntBits(sideways) : 0);
        result = 31 * result + (forward != +0.0f ? Float.floatToIntBits(forward) : 0);
        result = 31 * result + (jump ? 1 : 0);
        result = 31 * result + (unmount ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SteerVehicleMessage{" +
                "sideways=" + sideways +
                ", forward=" + forward +
                ", jump=" + jump +
                ", unmount=" + unmount +
                '}';
    }
}

