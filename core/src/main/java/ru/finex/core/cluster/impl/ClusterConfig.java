package ru.finex.core.cluster.impl;

import lombok.Data;
import org.redisson.config.TransportMode;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class ClusterConfig {

    private String redisAddress;
    @ConfigResource(nullable = true)
    private String username;
    @ConfigResource(nullable = true)
    private String password;

    private int retryAttempts;
    private long retryInterval;

    private long dnsMonitoringInterval;

    private int threads;

    private int database;

    private Subscriptions subscriptions;
    private Network network;

    private String role;

    @Data
    public static class Subscriptions {
        private int subscriptionsPerConnection;
        private int subscriptionConnectionMinimumIdleSize;
        private int subscriptionConnectionPoolSize;
    }

    @Data
    public static class Network {
        private int idleConnectionTimeout;
        private int connectTimeout;
        private int timeout;

        private int pingConnectionInterval;
        private boolean keepAlive;
        private boolean tcpNoDelay;

        private int connectionMinimumIdleSize;
        private int connectionPoolSize;

        private int networkThreads;

        private TransportMode transport;
    }

}
