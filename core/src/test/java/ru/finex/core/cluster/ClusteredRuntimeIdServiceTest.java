package ru.finex.core.cluster;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.finex.core.uid.impl.ClusteredRuntimeIdService;
import ru.finex.testing.container.Container;
import ru.finex.testing.container.ContainerType;
import ru.finex.testing.server.Server;

import java.util.concurrent.ThreadLocalRandom;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Container(ContainerType.Redis)
@Server(config = "cluster-test.conf", modules = ClusterModule.class)
public class ClusteredRuntimeIdServiceTest {

    private static final int MAX_IDS = 5_000;
    private static final int REPEATS = 5;

    @Inject
    private ClusteredRuntimeIdService service;

    @Test
    public void repeatableGenerateId() {
        // m0nster.mind: repeatable tests in junit is not working correctly with class rules
        for (int i = 0; i < REPEATS; i++) {
            generateIdTest();
        }
    }

    private void generateIdTest() {
        int maxIterations = ThreadLocalRandom.current().nextInt(MAX_IDS);
        for (int i = 1; i < maxIterations; i++) {
            int id = service.generateId();
            service.free(id);
            Assertions.assertEquals(i, id);
        }

        service.reset();
    }

    @Test
    public void outOfIndexTest() {
        long position = 0xffffffffL;

        service.reset(position);

        int id = service.generateId(); // 0xffffffff
        Assertions.assertEquals(-1, id, "Wrong ID: 0x" + Integer.toHexString(id));

        id = service.generateId();
        Assertions.assertEquals(1, id, "Wrong ID: 0x" + Integer.toHexString(id));
    }

}
