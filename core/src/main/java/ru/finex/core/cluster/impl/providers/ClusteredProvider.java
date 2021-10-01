package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public interface ClusteredProvider<T> {

    T get(Type type, RedissonClient client, String name);

}
