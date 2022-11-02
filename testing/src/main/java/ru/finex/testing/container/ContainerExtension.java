package ru.finex.testing.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.finex.testing.Utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

/**
 * @author m0nster.mind
 */
public class ContainerExtension implements AfterAllCallback, BeforeAllCallback {

    private static final ExtensionContext.Namespace CONTAINER = create("CONTAINER");
    private static final String CONTAINERS = "containers";
    private static final int REDIS_PORT = 6379;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Utils.findAnnotationRecursive(context, Container.class)
            .map(this::upContainers)
            .ifPresent(containers -> context.getStore(CONTAINER).put(CONTAINERS, containers));
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        List<GenericContainer<?>> containers = context.getStore(CONTAINER)
            .getOrDefault(CONTAINERS, List.class, Collections.emptyList());
        try {
            downContainers(containers);
        } finally {
            context.getStore(CONTAINER).remove(CONTAINERS);
        }
    }

    private List<GenericContainer<?>> upContainers(Container container) {
        return Stream.of(container.value())
                .map(this::upContainer)
                .collect(Collectors.toList());
    }

    private void downContainers(List<GenericContainer<?>> containers) {
        containers.forEach(GenericContainer::stop);
    }

    private GenericContainer<?> upContainer(ContainerType containerType) {
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
                .withExposedPorts(REDIS_PORT);
        redis.start();

        System.setProperty("REDIS_ADDRESS", String.format("redis://%s:%d", redis.getHost(), redis.getFirstMappedPort()));
        return redis;
    }

}
