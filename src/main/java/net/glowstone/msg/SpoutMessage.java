package net.glowstone.msg;

import org.getspout.spoutapi.packet.SpoutPacket;

/**
 * Message representing a wrapped Spoutcraft packet.
 */
public class SpoutMessage extends Message {
    
    private SpoutPacket packet;
    
    public SpoutMessage(SpoutPacket packet) {
        this.packet = packet;
    }
    
    public SpoutPacket getPacket() {
        return packet;
    }
    
}
