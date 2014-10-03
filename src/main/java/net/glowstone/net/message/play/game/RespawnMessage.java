package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public final class RespawnMessage implements Message {

    private final int dimension, difficulty, mode;
    private final String levelType;

    public RespawnMessage(int dimension, int difficulty, int mode, String levelType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.mode = mode;
        this.levelType = levelType;
    }

    public int getDimension() {
        return dimension;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMode() {
        return mode;
    }

    public String getLevelType() {
        return levelType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RespawnMessage that = (RespawnMessage) o;

        if (difficulty != that.difficulty) return false;
        if (dimension != that.dimension) return false;
        if (mode != that.mode) return false;
        if (levelType != null ? !levelType.equals(that.levelType) : that.levelType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dimension;
        result = 31 * result + difficulty;
        result = 31 * result + mode;
        result = 31 * result + (levelType != null ? levelType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RespawnMessage{" +
                "dimension=" + dimension +
                ", difficulty=" + difficulty +
                ", mode=" + mode +
                ", levelType='" + levelType + '\'' +
                '}';
    }
}
