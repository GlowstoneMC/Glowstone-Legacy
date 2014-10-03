package net.glowstone.net.message.login;

import com.flowpowered.networking.Message;

public final class LoginStartMessage implements Message {

    private final String username;

    public LoginStartMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginStartMessage that = (LoginStartMessage) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LoginStartMessage{" +
                "username='" + username + '\'' +
                '}';
    }
}
