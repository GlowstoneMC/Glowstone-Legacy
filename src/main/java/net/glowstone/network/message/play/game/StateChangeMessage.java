package net.glowstone.network.message.play.game;

import com.flowpowered.networking.Message;

public final class StateChangeMessage implements Message {

    public static enum Reason {
        INVALID_BED,
        STOP_RAIN,
        START_RAIN,
        GAMEMODE,
        CREDITS,
        DEMO_MESSAGE,
        ARROW,
        FADE_VALUE,
        FADE_TIME
    }

    private final int reason;
    private final float value;

    public StateChangeMessage(int reason, float value) {
        this.reason = reason;
        this.value = value;
    }

    public StateChangeMessage(Reason reason, float value) {
        this.reason = reason.ordinal();
        this.value = value;
    }

    public int getReason() {
        return reason;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StateChangeMessage{reason=" + reason + ",value=" + value + "}";
    }

}
