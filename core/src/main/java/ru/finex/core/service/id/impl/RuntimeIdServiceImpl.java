package ru.finex.core.service.id.impl;

import ru.finex.core.service.id.RuntimeIdService;

import java.util.BitSet;
import javax.inject.Singleton;

/**
 * TODO m0nster.mind: разбить генерацию локально по потокам
 *  каждый поток забирает себе 8 бит числа для идентификации, остальной промежуток остается за локальным генератором,
 *  и именно в нем должена работать непосредственная генерация.
 *
 * @author m0nster.mind
 */
@Singleton
public class RuntimeIdServiceImpl implements RuntimeIdService {

    private final BitSet bitSet = new BitSet();
    private int position;

    @Override
    public synchronized int generateId() {
        int id = position;

        bitSet.set(position);
        int nextPosition = bitSet.nextClearBit(position);
        position = nextPosition;

        return id;
    }

}
