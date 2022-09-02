package ru.finex.core.cluster;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.finex.core.ContainerRule;
import ru.finex.core.ContainerRule.Type;
import ru.finex.core.GlobalContext;
import ru.finex.core.ServerRule;
import ru.finex.core.ServerRule.Module;
import ru.finex.core.uid.impl.ClusteredRuntimeIdService;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author m0nster.mind
 */
public class ClusteredRuntimeIdServiceTest {

    private static final int MAX_IDS = 5_000;
    private static final int REPEATS = 5;

    @ClassRule(order = 0)
    public static ContainerRule containers = new ContainerRule(Type.Redis);

    @ClassRule(order = 1)
    public static ServerRule server = new ServerRule("cluster-test.conf", Module.of(ClusterModule.class));

    @Test
    public void repeatableGenerateId() {
        // m0nster.mind: repeatable tests in junit is not working correctly with class rules
        for (int i = 0; i < REPEATS; i++) {
            generateIdTest();
        }
    }

    private void generateIdTest() {
        var service = GlobalContext.injector.getInstance(ClusteredRuntimeIdService.class);
        int maxIterations = ThreadLocalRandom.current().nextInt(MAX_IDS);
        for (int i = 0; i < maxIterations; i++) {
            int id = service.generateId();
            service.free(id);
            Assertions.assertEquals(i, id);
        }

        service.reset();
    }

    @Test
    public void outOfIndexTest() {
        var service = GlobalContext.injector.getInstance(ClusteredRuntimeIdService.class);
        long position = 0xffffffffL;

        service.reset(position);

        int id = service.generateId(); // 0xffffffff
        Assertions.assertEquals(-1, id, "Wrong ID: 0x" + Integer.toHexString(id));

        id = service.generateId();
        Assertions.assertEquals(0, id, "Wrong ID: 0x" + Integer.toHexString(id));
    }

}
