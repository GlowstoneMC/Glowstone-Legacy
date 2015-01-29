package net.glowstone.net.flow;

/**
 * Interface for codec lookup services.
 */
public interface CodecLookup {

    /**
     * Finds a codec by its opcode.
     * @param opcode The opcode.
     * @return The codec, or {@code null} if it could not be found.
     */
    Codec<?> find(int opcode);

    /**
     * Finds a codec by message class.
     * @param clazz The message class.
     * @param <M> The type of message.
     * @return The codec, or {@code null} if it could not be found.
     */
    <M extends Message> CodecRegistration<M> find(Class<M> clazz);

}
