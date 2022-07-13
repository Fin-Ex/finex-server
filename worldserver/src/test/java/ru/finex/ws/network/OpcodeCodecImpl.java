package ru.finex.ws.network;

import io.netty.buffer.ByteBuf;
import ru.finex.network.netty.serial.OpcodeCodec;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class OpcodeCodecImpl implements OpcodeCodec {

    @Override
    public void encode(int[] opcodes, ByteBuf buffer) {
        for (int i = 0; i < opcodes.length; i++) {
            buffer.writeIntLE(opcodes[i]);
        }
    }

    @Override
    public int[] decode(ByteBuf buffer) {
        return new int[] { buffer.readIntLE() };
    }

}
