package net.glowstone.spout;

import java.util.HashMap;
import java.util.ArrayList;
import org.getspout.spoutapi.packet.PacketManager;
import org.getspout.spoutapi.packet.listener.PacketListener;
import org.getspout.spoutapi.packet.standard.MCPacket;

/**
 * Packet manager for Spout integration.
 */
public class GlowPacketManager implements PacketManager {
    
    private static final int UNCOMPRESSED_ID = -1;
    private final HashMap<Integer, ArrayList<PacketListener>> listeners = new HashMap<Integer, ArrayList<PacketListener>>();
    
    // add

    public void addListener(int packetId, PacketListener listener) {
        getList(packetId).add(listener);
    }

    public void addListenerUncompressedChunk(PacketListener listener) {
        getList(UNCOMPRESSED_ID).add(listener);
    }
    
    // remove

    public boolean removeListener(int packetId, PacketListener listener) {
        return getList(packetId).remove(listener);
    }

    public boolean removeListenerUncompressedChunk(PacketListener listener) {
        return getList(UNCOMPRESSED_ID).remove(listener);
    }

    public void clearAllListeners() {
        listeners.clear();
    }
    
    // misc

    public MCPacket getInstance(int packetId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private ArrayList<PacketListener> getList(int packetId) {
        if (!listeners.containsKey(packetId)) {
            listeners.put(packetId, new ArrayList<PacketListener>());
        }
        return listeners.get(packetId);
    }
    
}
