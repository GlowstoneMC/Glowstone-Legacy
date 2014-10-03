package net.glowstone.net.message.handshake;

import com.flowpowered.networking.AsyncableMessage;

public final class HandshakeMessage implements AsyncableMessage {

    private final int version;
    private final String address;
    private final int port;
    private final int state;

    public HandshakeMessage(int version, String address, int port, int state) {
        this.version = version;
        this.address = address;
        this.port = port;
        this.state = state;
    }

    public int getVersion() {
        return version;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getState() {
        return state;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HandshakeMessage that = (HandshakeMessage) o;

        if (port != that.port) return false;
        if (state != that.state) return false;
        if (version != that.version) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + state;
        return result;
    }

    @Override
    public String toString() {
        return "HandshakeMessage{" +
                "version=" + version +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", state=" + state +
                '}';
    }
}
