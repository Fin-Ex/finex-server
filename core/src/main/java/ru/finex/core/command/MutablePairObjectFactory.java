package ru.finex.core.command;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Mutable pair factory for commons pool.
 *
 * @param <K> pair left type
 * @param <V> pair right type
 * @author m0nster.mind
 */
public class MutablePairObjectFactory<K, V> extends BasePooledObjectFactory<Pair<K, V>> {

    @Override
    public Pair<K, V> create() throws Exception {
        return new MutablePair<>();
    }

    @Override
    public PooledObject<Pair<K, V>> wrap(Pair<K, V> obj) {
        return new DefaultPooledObject<>(obj);
    }

}
