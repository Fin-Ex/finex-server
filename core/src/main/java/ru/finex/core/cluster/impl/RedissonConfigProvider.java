package ru.finex.core.cluster.impl;

import org.redisson.config.Config;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class RedissonConfigProvider implements Provider<Config> {

    private Config config;

    @Inject
    public RedissonConfigProvider(ClusterConfig clusterConfig) {
        config = new Config();
        config.setCodec(new Kryo5Codec())
            .setTransportMode(clusterConfig.getNetwork().getTransport())
            .setThreads(clusterConfig.getThreads())
            .setNettyThreads(clusterConfig.getNetwork().getNetworkThreads())
            .useSingleServer()
            .setAddress(clusterConfig.getRedisAddress())
            .setUsername(clusterConfig.getUsername())
            .setPassword(clusterConfig.getPassword())
            .setDatabase(clusterConfig.getDatabase())
            .setConnectionPoolSize(clusterConfig.getNetwork().getConnectionPoolSize())
            .setConnectionMinimumIdleSize(clusterConfig.getNetwork().getConnectionMinimumIdleSize())
            .setDnsMonitoringInterval(clusterConfig.getDnsMonitoringInterval())
            .setSubscriptionConnectionPoolSize(clusterConfig.getSubscriptions().getSubscriptionConnectionPoolSize())
            .setSubscriptionConnectionMinimumIdleSize(clusterConfig.getSubscriptions().getSubscriptionConnectionMinimumIdleSize())
            .setSubscriptionsPerConnection(clusterConfig.getSubscriptions().getSubscriptionsPerConnection())
            .setPingConnectionInterval(clusterConfig.getNetwork().getPingConnectionInterval())
            .setIdleConnectionTimeout(clusterConfig.getNetwork().getIdleConnectionTimeout())
            .setConnectTimeout(clusterConfig.getNetwork().getConnectTimeout())
            .setTimeout(clusterConfig.getNetwork().getTimeout())
            .setKeepAlive(clusterConfig.getNetwork().isKeepAlive())
            .setTcpNoDelay(clusterConfig.getNetwork().isTcpNoDelay());
    }

    @Override
    public Config get() {
        return config;
    }

}
