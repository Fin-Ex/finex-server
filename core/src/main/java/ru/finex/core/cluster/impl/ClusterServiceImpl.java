package ru.finex.core.cluster.impl;

import lombok.Getter;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RObject;
import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.ClusterService;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClusterServiceImpl implements ClusterService {

    private final Set<RObject> allocatedObjects = new HashSet<>();

    @Getter
    private final RedissonClient client;
    @Getter
    private final String role;
    private final RAtomicLong instances;

    @Inject
    public ClusterServiceImpl(RedissonClient client, ClusterConfig config) {
        this.client = client;
        this.role = config.getRole();
        this.instances = client.getAtomicLong(getAddress(getClass(), "instances"));
    }

    @Override
    public String getAddress(String name) {
        return role + "@" + name;
    }

    @Override
    public String getAddress(Class<?> caller) {
        return role + "@" + caller.getCanonicalName();
    }

    @Override
    public String getAddress(Class<?> caller, String field) {
        return getAddress(caller) + "#" + field;
    }

    @Override
    public String getAddress(Class<?> caller, String method, String parameter) {
        return role + "@" + caller.getCanonicalName() + "::" + method + "#" + parameter;
    }

    @Override
    public int getInstances() {
        return (int) instances.get();
    }

    @Override
    public void registerManagedResource(RObject resource) {
        allocatedObjects.add(resource);
    }

    @PostConstruct
    private void start() {
        // TODO m0nster.mind: send event to channel about new instance
        instances.incrementAndGet();
    }

    @PreDestroy
    private void destroy() {
        if (instances.decrementAndGet() == 0) {
            allocatedObjects.forEach(RObject::delete);
        }
        client.shutdown();
    }

}
