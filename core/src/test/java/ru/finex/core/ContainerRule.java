package ru.finex.core;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author m0nster.mind
 */
public class ContainerRule implements TestRule {

    public enum Type {
        Redis,
        Database
    }

    private final Type[] types;
    private List<GenericContainer<?>> containers = Collections.emptyList();

    public ContainerRule(Type...types) {
        this.types = types;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                upContainers();
                try {
                    statement.evaluate();
                } finally {
                    downContainers();
                }
            }
        };
    }

    private void upContainers() {
        containers = Stream.of(types)
            .map(this::upContainer)
            .collect(Collectors.toList());
    }

    private void downContainers() {
        containers.forEach(GenericContainer::stop);
        containers = Collections.emptyList();
    }

    private GenericContainer<?> upContainer(Type containerType) {
        return switch (containerType) {
            case Database -> createDatabase();
            case Redis -> createRedis();
        };
    }

    private GenericContainer<?> createDatabase() {
        var db = new PostgreSQLContainer<>(DockerImageName.parse(PostgreSQLContainer.IMAGE).withTag("12-alpine"));
        db.start();

        System.setProperty("DB_SCHEMA", "public");
        System.setProperty("DB_URL", db.getJdbcUrl());
        System.setProperty("DB_USER", db.getUsername());
        System.setProperty("DB_PASSWORD", db.getPassword());
        return db;
    }

    private GenericContainer<?> createRedis() {
        var redis = new GenericContainer<>(DockerImageName.parse("redis").withTag("6.2.6-alpine"))
            .withExposedPorts(6379);
        redis.start();

        System.setProperty("REDIS_ADDRESS", String.format("redis://%s:%d", redis.getHost(), redis.getFirstMappedPort()));
        return redis;
    }

}
