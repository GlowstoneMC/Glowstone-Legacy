package net.glowstone.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import net.glowstone.EventFactory;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.KickMessage;
import net.glowstone.msg.Message;
import net.glowstone.msg.handler.HandlerLookupService;
import net.glowstone.msg.handler.MessageHandler;
import org.bukkit.event.player.PlayerKickEvent;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * A single connection to the server, which may or may not be associated with a
 * player.
 * @author Graham Edgecombe
 */
public final class Session {

    /**
     * The number of ticks which are elapsed before a client is disconnected due
     * to a timeout.
     */
    private static final int TIMEOUT_TICKS = 300;

    /**
     * The state this connection is currently in.
     */
    public enum State {

        /**
         * In the exchange handshake state, the server is waiting for the client
         * to send its initial handshake packet.
         */
        EXCHANGE_HANDSHAKE,

        /**
         * In the exchange identification state, the server is waiting for the
         * client to send its identification packet.
         */
        EXCHANGE_IDENTIFICATION,

        /**
         * In the game state the session has an associated player.
         */
        GAME;
    }

    /**
     * The server this session belongs to.
     */
    private final GlowServer server;

    /**
     * The channel associated with this session.
     */
    private final Channel channel;

    /**
     * A queue of incoming and unprocessed messages.
     */
    private final Queue<Message> messageQueue = new ArrayDeque<Message>();

    /**
     * A timeout counter. This is increment once every tick and if it goes above
     * a certain value the session is disconnected.
     */
    private int timeoutCounter = 0;

    /**
     * The current state.
     */
    private State state = State.EXCHANGE_HANDSHAKE;

    /**
     * The player associated with this session (if there is one).
     */
    private GlowPlayer player;

    /**
     * The random long used for client-server handshake
     */

    private String sessionId = Long.toHexString(new Random().nextLong());

    /**
     * Creates a new session.
     * @param server The server this session belongs to.
     * @param channel The channel associated with this session.
     */
    public Session(GlowServer server, Channel channel) {
        this.server = server;
        this.channel = channel;
    }

    /**
     * Gets the state of this session.
     * @return The session's state.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of this session.
     * @param state The new state.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Gets the player associated with this session.
     * @return The player, or {@code null} if no player is associated with it.
     */
    public GlowPlayer getPlayer() {
        return player;
    }

    /**
     * Sets the player associated with this session.
     * @param player The new player.
     * @throws IllegalStateException if there is already a player associated
     * with this session.
     */
    public void setPlayer(GlowPlayer player) {
        if (this.player != null)
            throw new IllegalStateException();

        this.player = player;
        ((GlowWorld) this.server.getWorlds().get(0)).getRawPlayers().add(player);
        
        GlowServer.logger.log(Level.INFO, "{0} joined the game", player.getName());

        String message = EventFactory.onPlayerJoin(player).getJoinMessage();
        if (message != null) {
            server.broadcastMessage(message);
        }
    }

    @SuppressWarnings("unchecked")
    public void pulse() {
        timeoutCounter++;

        Message message;
        while ((message = messageQueue.poll()) != null) {
            MessageHandler<Message> handler = (MessageHandler<Message>) HandlerLookupService.find(message.getClass());
            if (handler != null) {
                handler.handle(this, player, message);
            }
            timeoutCounter = 0;
        }

        if (timeoutCounter >= TIMEOUT_TICKS)
            disconnect("Timed out");
    }

    /**
     * Sends a message to the client.
     * @param message The message.
     */
    public void send(Message message) {
        channel.write(message);
    }

    /**
     * Disconnects the session with the specified reason. This causes a
     * {@link KickMessage} to be sent. When it has been delivered, the channel
     * is closed.
     * @param reason The reason for disconnection.
     */
    public void disconnect(String reason) {
        disconnect(reason, false);
    }
    
    /**
     * Disconnects the session with the specified reason. This causes a
     * {@link KickMessage} to be sent. When it has been delivered, the channel
     * is closed.
     * @param reason The reason for disconnection.
     * @param overrideKick Whether to override the kick event.
     */
    public void disconnect(String reason, boolean overrideKick) {
        if (player != null && !overrideKick) {
            GlowServer.logger.log(Level.INFO, "{0} left the game", player.getName());

            PlayerKickEvent event = EventFactory.onPlayerKick(player, reason);
            if (event.isCancelled()) {
                return;
            }

            reason = event.getReason();

            if (event.getLeaveMessage() != null) {
                server.broadcastMessage(event.getLeaveMessage());
            }
        }
    
        channel.write(new KickMessage(reason)).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Gets the server associated with this session.
     * @return The server.
     */
    public GlowServer getServer() {
        return server;
    }
    
    /**
     * Returns the address of this session.
     * @return The remote address.
     */
    public InetSocketAddress getAddress() {
        SocketAddress addr = channel.getRemoteAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return Session.class.getName() + " [address=" + channel.getRemoteAddress() + "]";
    }

    /**
     * Adds a message to the unprocessed queue.
     * @param message The message.
     * @param <T> The type of message.
     */
    <T extends Message> void messageReceived(T message) {
        messageQueue.add(message);
    }

    /**
     * Disposes of this session by destroying the associated player, if there is
     * one.
     */
    void dispose() {
        if (player != null) {            
            player.remove();
            player = null; // in case we are disposed twice
        }
    }

    public String getSessionId() {
        return sessionId;
    }

}
