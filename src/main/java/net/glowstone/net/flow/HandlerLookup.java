package net.glowstone.net.flow;

/**
 * A class which can provide {@link MessageHandler}s for message classes.
 */
public interface HandlerLookup {

    /**
     * Get a message handler for a given message class.
     * @param clazz The message class to look up.
     * @return The message handler, or null if not found.
     */
    <M extends Message> MessageHandler<?, M> getHandler(Class<M> clazz);

}
