package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public final class PlayerActionMessage implements Message {

    private final int id;
    private final int action;
    private final int jumpBoost;

    public PlayerActionMessage(int id, int action, int jumpBoost) {
        this.id = id;
        this.action = action;
        this.jumpBoost = jumpBoost;
    }

    public int getId() {
        return id;
    }

    public int getAction() {
        return action;
    }

    public int getJumpBoost() {
        return jumpBoost;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerActionMessage that = (PlayerActionMessage) o;

        if (action != that.action) return false;
        if (id != that.id) return false;
        if (jumpBoost != that.jumpBoost) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + action;
        result = 31 * result + jumpBoost;
        return result;
    }

    @Override
    public String toString() {
        return "PlayerActionMessage{" +
                "id=" + id +
                ", action=" + action +
                ", jumpBoost=" + jumpBoost +
                '}';
    }

}

