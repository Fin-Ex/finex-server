package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.impl.BucketAdapter;
import ru.finex.core.utils.Holder;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredHolderProvider implements ClusteredProvider<Holder> {

    @Override
    public Holder get(Type type, RedissonClient client, String name) {
        return new BucketAdapter(client.getBucket(name));
    }

}
