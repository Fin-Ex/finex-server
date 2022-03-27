package ru.finex.core.network;

import com.google.inject.spi.InjectionListener;
import lombok.RequiredArgsConstructor;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Provider;

/**
 * Serializer/deserializer post processor. Register serial in packet service.
 * @param <I> serializer or deserializer type
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class PacketPostProcessor<I> implements InjectionListener<I> {

    private final Provider<PacketService> packetServiceProvider;

    @Override
    public void afterInjection(I injectee) {
        PacketService packetService = packetServiceProvider.get();
        if (injectee instanceof PacketDeserializer deserializer) {
            packetService.saveDeserializer(deserializer);
        } else {
            packetService.saveSerializer((PacketSerializer) injectee);
        }
    }

}
