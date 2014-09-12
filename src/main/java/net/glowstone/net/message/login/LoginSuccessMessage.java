package net.glowstone.net.message.login;

import com.flowpowered.networking.Message;

public final class LoginSuccessMessage implements Message {

    private final String uuid;
    private final String username;

    public LoginSuccessMessage(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LoginSuccessMessage{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginSuccessMessage that = (LoginSuccessMessage) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
