package net.glowstone.net.message;

import com.flowpowered.networking.Message;

public final class SetCompressionMessage implements Message {

    private final int threshold;

    public SetCompressionMessage(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }

    @Override
    public String toString() {
        return "SetCompressionMessage{" +
                "threshold=" + threshold +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetCompressionMessage that = (SetCompressionMessage) o;

        if (threshold != that.threshold) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return threshold;
    }
}

