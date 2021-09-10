package ru.finex.core.cluster.impl;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class HazelcastProvider implements Provider<HazelcastInstance> {

    private final HazelcastInstance instance;

    @Inject
    public HazelcastProvider(Config config) {
        this.instance = Hazelcast.newHazelcastInstance(config);
    }

    @Override
    public HazelcastInstance get() {
        return instance;
    }
}
