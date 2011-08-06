package net.glowstone.net.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import net.glowstone.GlowServer;

import org.getspout.spoutapi.packet.*;

import net.glowstone.msg.SpoutMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class SpoutCodec extends MessageCodec<SpoutMessage> {

    public SpoutCodec() {
        super(SpoutMessage.class, 0xC3);
    }

    @Override
    public SpoutMessage decode(ChannelBuffer buffer) throws IOException {
        int id = buffer.readInt();
        int size = buffer.readInt();
        byte[] data = new byte[size];
        buffer.readBytes(data);
        
        PacketType packetType = PacketType.getPacketFromId(id);
        SpoutPacket packet = null;
        try {
            Constructor<? extends SpoutPacket> constructor = packetType.getPacketClass().getConstructor();
            packet = constructor.newInstance();
        } catch (Exception ex) {
            GlowServer.logger.log(Level.SEVERE, "Error parsing Spoutcraft packet: {0}", ex.getMessage());
            ex.printStackTrace();
        }
        
        if (packet == null) {
            return null;
        }
        ByteArrayInputStream bytes = new ByteArrayInputStream(data);
        packet.readData(new DataInputStream(bytes));
        
        if (packetType != PacketType.PacketKeyPress) {
            System.out.println("[debug] Got Spout packet " + packetType.toString());
        }
        
        return new SpoutMessage(packet);
    }

    @Override
    public ChannelBuffer encode(SpoutMessage message) throws IOException {
        SpoutPacket packet = message.getPacket();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        packet.writeData(new DataOutputStream(bytes));
        
        ChannelBuffer buffer = ChannelBuffers.buffer(8 + packet.getNumBytes());
        buffer.writeInt(packet.getPacketType().getId());
        buffer.writeInt(packet.getNumBytes());
        buffer.writeBytes(bytes.toByteArray());
        
        System.out.println("[debug] Encoded Spout packet " + packet.getPacketType().toString());
        
        return buffer;
    }

}
