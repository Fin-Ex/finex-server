package ru.finex.network.netty.serial;

import io.netty.buffer.ByteBuf;

/**
 * Packet operation code (packet ID) codec.
 *
 * @author m0nster.mind
 */
public interface OpcodeCodec {

    /**
     * Write opcodes to buffer.
     * @param opcodes opcodes
     * @param buffer buffer
     */
    void encode(int[] opcodes, ByteBuf buffer);

    /**
     * Read opcodes from buffer.
     * @param buffer buffer
     * @return opcodes
     */
    int[] decode(ByteBuf buffer);

}
