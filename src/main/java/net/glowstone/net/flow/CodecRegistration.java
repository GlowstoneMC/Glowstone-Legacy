package net.glowstone.net.flow;

/**
 * Todo: Javadoc for CodecRegistration.
 */
public final class CodecRegistration<M extends Message> {
    private final int opcode;
    private final Codec<? super M> codec;

    public CodecRegistration(int opcode, Codec<? super M> codec) {
        this.opcode = opcode;
        this.codec = codec;
    }

    public int getOpcode() {
        return opcode;
    }

    public Codec<? super M> getCodec() {
        return codec;
    }

}
