package ru.finex.core.cluster.impl;

import lombok.Getter;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.ClusterService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClusterServiceImpl implements ClusterService {

    @Getter
    private final RedissonClient client;
    @Getter
    private final String role;
    private final RAtomicLong instances;

    @Inject
    public ClusterServiceImpl(RedissonClient client, ClusterConfig config) {
        this.client = client;
        role = config.getRole();
        instances = client.getAtomicLong(getName(getClass(), "instances"));
    }

    @Override
    public String getName(String name) {
        return role + "@" + name;
    }

    @Override
    public String getName(Class<?> caller) {
        return role + "@" + caller.getCanonicalName();
    }

    @Override
    public String getName(Class<?> caller, String field) {
        return getName(caller) + "#" + field;
    }

    @Override
    public int getInstances() {
        return (int) instances.get();
    }

    @PostConstruct
    private void start() {
        // TODO m0nster.mind: send event to channel about new instance
        instances.incrementAndGet();
    }

    @PreDestroy
    private void destroy() {
        instances.decrementAndGet();
        client.shutdown();
    }

}
