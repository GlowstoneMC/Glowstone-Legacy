package net.glowstone.net;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A list of all the sessions which provides a convenient {@link #pulse()}
 * method to pulse every session in one operation.
 * @author Graham Edgecombe
 */
public final class SessionRegistry {

    /**
     * A list of the sessions.
     */
    private final CopyOnWriteArrayList<GlowSession> sessions = new CopyOnWriteArrayList<>();

    /**
     * Pulses all the sessions.
     */
    public void pulse() {
        for (GlowSession session : sessions) {
            session.pulse();
        }
    }

    /**
     * Adds a new session.
     * @param session The session to add.
     */
    public void add(GlowSession session) {
        sessions.add(session);
    }

    /**
     * Removes a session.
     * @param session The session to remove.
     */
    public void remove(GlowSession session) {
        sessions.remove(session);
    }

}
