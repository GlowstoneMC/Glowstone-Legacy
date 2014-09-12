package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public final class JoinGameMessage implements Message {

    private final int id, dimension, mode, difficulty, maxPlayers;
    private final String levelType;
    private final boolean reducedDebugInfo;

    public JoinGameMessage(int id, int mode, int dimension, int difficulty, int maxPlayers, String levelType, boolean reducedDebug) {
        this.id = id;
        this.mode = mode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        // default, flat, largeBiomes, amplified, default_1_1
        this.levelType = levelType;
        this.reducedDebugInfo = reducedDebug;
    }

    public int getId() {
        return id;
    }

    public int getGameMode() {
        return mode;
    }

    public int getDimension() {
        return dimension;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getLevelType() {
        return levelType;
    }

    public boolean getReducedDebugInfo() {
        return reducedDebugInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinGameMessage that = (JoinGameMessage) o;

        if (difficulty != that.difficulty) return false;
        if (dimension != that.dimension) return false;
        if (id != that.id) return false;
        if (maxPlayers != that.maxPlayers) return false;
        if (mode != that.mode) return false;
        if (reducedDebugInfo != that.reducedDebugInfo) return false;
        if (levelType != null ? !levelType.equals(that.levelType) : that.levelType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + dimension;
        result = 31 * result + mode;
        result = 31 * result + difficulty;
        result = 31 * result + maxPlayers;
        result = 31 * result + (levelType != null ? levelType.hashCode() : 0);
        result = 31 * result + (reducedDebugInfo ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JoinGameMessage{" +
                "id=" + id +
                ", dimension=" + dimension +
                ", mode=" + mode +
                ", difficulty=" + difficulty +
                ", maxPlayers=" + maxPlayers +
                ", levelType='" + levelType + '\'' +
                '}';
    }

}
