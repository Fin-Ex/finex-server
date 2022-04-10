package ru.finex.core.network;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;

/**
 * @author m0nster.mind
 */
public class PacketListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<? super I> rawType = type.getRawType();
        if (!PacketSerializer.class.isAssignableFrom(rawType) && !PacketDeserializer.class.isAssignableFrom(rawType)) {
            return;
        }

        Provider<PacketService> packetServiceProvider = encounter.getProvider(PacketService.class);
        encounter.register(new PacketPostProcessor<>(packetServiceProvider));
    }

}
