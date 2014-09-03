package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public class ServerDifficultyMessage implements Message {

    private int difficulty;

    public ServerDifficultyMessage(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "ServerDifficultyMessage{" +
                "difficulty=" + difficulty +
                '}';
    }
}
