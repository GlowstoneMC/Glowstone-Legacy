package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public final class HealthMessage implements Message {

    private final float health;
    private final int food;
    private final float saturation;

    public HealthMessage(float health, int food, float saturation) {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    public float getHealth() {
        return health;
    }

    public int getFood() {
        return food;
    }

    public float getSaturation() {
        return saturation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HealthMessage that = (HealthMessage) o;

        if (food != that.food) return false;
        if (Float.compare(that.health, health) != 0) return false;
        if (Float.compare(that.saturation, saturation) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (health != +0.0f ? Float.floatToIntBits(health) : 0);
        result = 31 * result + food;
        result = 31 * result + (saturation != +0.0f ? Float.floatToIntBits(saturation) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HealthMessage{health=" + health + ",food=" + food + ",saturation=" + saturation + "}";
    }

}
