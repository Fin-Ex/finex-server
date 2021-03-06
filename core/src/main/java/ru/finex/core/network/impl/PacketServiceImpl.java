package ru.finex.core.network.impl;

import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.core.network.PacketMetadata;
import ru.finex.core.network.PacketService;
import ru.finex.core.utils.GenericUtils;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PacketServiceImpl implements PacketService {

    private final Map<Integer, Object> incomeRegistry = new HashMap<>();
    private final Map<Integer, Object> outcomeRegistry = new HashMap<>();
    private final Map<Class<? extends NetworkDto>, PacketMetadata<PacketSerializer<?>>> outcomeDataTypeRegistry = new HashMap<>();

    public PacketServiceImpl() {
    }

    @Override
    public void saveDeserializer(PacketDeserializer<?> deserializer) {
        Class<? extends PacketDeserializer> type = deserializer.getClass();
        IncomePacket metadata = type.getAnnotation(IncomePacket.class);
        if (metadata == null) {
            throw new RuntimeException("@IncomePacket at type '" + type.getCanonicalName() + "' not found!");
        }

        int[] opcodes = getOpcodes(metadata.value());
        Class[] commands = Stream.of(metadata.command())
            .map(Cmd::value)
            .toArray(Class[]::new);

        Class<? extends NetworkDto> dataType = GenericUtils.getInterfaceGenericType(type, PacketDeserializer.class, 0);
        saveDeserializer(new PacketMetadata<>(opcodes, commands, dataType, deserializer));
    }

    private void saveDeserializer(PacketMetadata<PacketDeserializer<?>> packetMetadata) {
        saveSerial(incomeRegistry, packetMetadata);
    }

    @Override
    public void saveSerializer(PacketSerializer<?> serializer) {
        Class<? extends PacketSerializer> type = serializer.getClass();
        OutcomePacket metadata = type.getAnnotation(OutcomePacket.class);
        if (metadata == null) {
            throw new RuntimeException("@OutcomePacket at type '" + type.getCanonicalName() + "' not found!");
        }

        int[] opcodes = getOpcodes(metadata.value());
        Class<? extends NetworkDto> dataType = GenericUtils.getInterfaceGenericType(type, PacketSerializer.class, 0);

        PacketMetadata<PacketSerializer<?>> packetMetadata = new PacketMetadata<>(opcodes, dataType, serializer);
        saveSerializer(packetMetadata);
        outcomeDataTypeRegistry.put(dataType, packetMetadata);
    }

    private void saveSerializer(PacketMetadata<PacketSerializer<?>> packetMetadata) {
        saveSerial(outcomeRegistry, packetMetadata);
    }

    private void saveSerial(Map<Integer, Object> registry, PacketMetadata<?> packetMetadata) {
        Map<Integer, Object> map = registry;
        int[] opcodes = packetMetadata.getOpcodes();
        for (int i = 0; i < opcodes.length - 1; i++) {
            int opcode = opcodes[i];
            map = (Map<Integer, Object>) map.computeIfAbsent(opcode, e -> new HashMap<Integer, Object>());
        }

        map.put(packetMetadata.getLastOpcode(), packetMetadata);
    }

    private int[] getOpcodes(Opcode[] opcodes) {
        return Stream.of(opcodes)
            .mapToInt(Opcode::value)
            .toArray();
    }

    @Override
    public PacketMetadata<PacketDeserializer<?>> getIncomePacketMetadata(int... opcodes) {
        return (PacketMetadata<PacketDeserializer<?>>) getPacketMetadata(incomeRegistry, opcodes);
    }

    @Override
    public PacketMetadata<PacketSerializer<?>> getOutcomePacketMetadata(int... opcodes) {
        return (PacketMetadata<PacketSerializer<?>>) getPacketMetadata(outcomeRegistry, opcodes);
    }

    @Override
    public <T extends NetworkDto> PacketMetadata<PacketSerializer<?>> getOutcomePacketMetadata(Class<T> dataType) {
        return outcomeDataTypeRegistry.get(dataType);
    }

    private PacketMetadata<?> getPacketMetadata(Map<Integer, Object> registry, int[] opcodes) {
        Map<Integer, Object> map = registry;
        for (int i = 0; i < opcodes.length; i++) {
            int opcode = opcodes[i];

            Object raw = map.get(opcode);
            if (raw == null) {
                return null;
            }

            if (raw instanceof PacketMetadata packetMetadata) {
                if (i == opcodes.length - 1) {
                    return packetMetadata;
                }

                break;
            } else {
                map = (Map<Integer, Object>) raw;
            }
        }

        throw new RuntimeException("Packet with opcodes " + Arrays.toString(opcodes) + " is not registered!");
    }

}
