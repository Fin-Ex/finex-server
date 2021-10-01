package ru.finex.core.cluster.impl;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.serializers.JavaSerializer;
import com.esotericsoftware.kryo.kryo5.util.Pool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

/**
 * The port of {@link org.redisson.codec.Kryo5Codec} for Kryo 5.2.0 and highest.
 *
 * @author m0nster.mind
 */
public class Kryo5Codec extends BaseCodec {

    private final Pool<Kryo> kryoPool;
    private final Pool<Input> inputPool;
    private final Pool<Output> outputPool;

    public Kryo5Codec() {
        this(null);
    }

    public Kryo5Codec(ClassLoader classLoader, Kryo5Codec codec) {
        this(classLoader);
    }

    public Kryo5Codec(ClassLoader classLoader) {

        this.kryoPool = new Pool<>(true, false, 1024) {
            @Override
            protected Kryo create() {
                return createKryo(classLoader);
            }
        };

        this.inputPool = new Pool<>(true, false, 512) {
            @Override
            protected Input create() {
                return new Input(8192);
            }
        };

        this.outputPool = new Pool<>(true, false, 512) {
            @Override
            protected Output create() {
                return new Output(8192, -1);
            }
        };
    }

    protected Kryo createKryo(ClassLoader classLoader) {
        Kryo kryo = new Kryo();
        if (classLoader != null) {
            kryo.setClassLoader(classLoader);
        }
        kryo.setRegistrationRequired(false);
        kryo.setReferences(false);
        kryo.addDefaultSerializer(Throwable.class, new JavaSerializer());
        return kryo;
    }

    private final Decoder<Object> decoder = new Decoder<>() {
        @Override
        public Object decode(ByteBuf buf, State state) {
            Kryo kryo = kryoPool.obtain();
            Input input = inputPool.obtain();
            try {
                input.setInputStream(new ByteBufInputStream(buf));
                return kryo.readClassAndObject(input);
            } finally {
                kryoPool.free(kryo);
                inputPool.free(input);
            }
        }
    };

    private final Encoder encoder = new Encoder() {
        @Override
        public ByteBuf encode(Object in) {
            Kryo kryo = kryoPool.obtain();
            Output output = outputPool.obtain();
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            try {
                ByteBufOutputStream baos = new ByteBufOutputStream(out);
                output.setOutputStream(baos);
                kryo.writeClassAndObject(output, in);
                output.flush();
                return baos.buffer();
            } catch (RuntimeException e) {
                out.release();
                throw e;
            } finally {
                kryoPool.free(kryo);
                outputPool.free(output);
            }
        }
    };

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

}
