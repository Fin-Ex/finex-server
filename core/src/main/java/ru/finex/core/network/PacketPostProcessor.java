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
            Class<?> type = injectee.getClass();
            IncomePacket incomePacketInfo = type.getAnnotation(IncomePacket.class);

            if (incomePacketInfo != null && incomePacketInfo.autoRegister()) {
                packetService.saveDeserializer(deserializer);
            }
        } else {
            Class<?> type = injectee.getClass();
            OutcomePacket outcomePacketInfo = type.getAnnotation(OutcomePacket.class);

            if (outcomePacketInfo != null && outcomePacketInfo.autoRegister()) {
                packetService.saveSerializer((PacketSerializer) injectee);
            }
        }
    }

}
