package ru.finex.core.math.bvh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import ru.finex.core.math.bvh.aabb.Box2Tree;
import ru.finex.core.math.bvh.aabb.Box2Tree.Node;
import ru.finex.core.math.bvh.aabb.Box2TreeElement;
import ru.finex.core.math.shape.impl.Box2f;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.impl.ArrayDequePool;
import ru.finex.core.pool.impl.SimplePooledObjectFactory;
import ru.finex.core.rng.RandomProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author m0nster.mind
 */
@Slf4j
public class Box2TreeTest {

    private PoolService poolService;
    private Box2Tree worldTree;

    public Box2TreeTest() throws Exception {
        ObjectPool<Node> pool = new ArrayDequePool<>(new SimplePooledObjectFactory<>(Node.class), 0, true);
        for (int i = 0; i < 2000; i++) {
            pool.addObject();
        }

        poolService = mock(PoolService.class);
        when(poolService.createDynamicPool(eq(Node.class), any()))
            .thenReturn(pool);

        worldTree = new Box2Tree(poolService, null);
    }

    @AfterEach
    public void clearTree() {
        worldTree.clear();
    }

    @RepeatedTest(10)
    public void testQueryCircle() throws Exception {
        List<Box2TreeElement> shapes = generateShapes(1500, 1, 1, 150);
        worldTree.build(shapes);

        var baseBox = shapes.get(0).getBoundingBox();
        var point = new Vector2f(baseBox.xmin + baseBox.getWidth() / 2, baseBox.ymin + baseBox.getHeight() / 2);

        List<Integer> expected = shapes.stream()
            .filter(e -> e.getBoundingBox().contains(point) || e.getBoundingBox().intersects(point.getX(), point.getY(), 100))
            .map(Box2TreeElement::getId)
            .collect(Collectors.toList());

        log.info("Selected {} shapes", expected.size());

        List<Integer> actual = worldTree.queryCircle(point, 100);

        Assertions.assertEquals(expected.size(), actual.size(), "Tree query return invalid result: '" + actual + "', expected: '" + expected + "'.");

        List<Integer> remains = new ArrayList<>(expected);
        remains.removeAll(actual);
        Assertions.assertTrue(remains.isEmpty(), "Tree query return invalid result: '" + actual + "', expected: '" + expected + "'.");

        remains = new ArrayList<>(actual);
        remains.removeAll(expected);
        Assertions.assertTrue(remains.isEmpty(), "Tree query return invalid result: '" + actual + "', expected: '" + expected + "'.");
    }

    private List<Box2TreeElement> generateShapes(int count, float maxX, float maxY, float maxRadius) {
        Random rng = RandomProvider.defaultRandom().get();
        Vector3f basePoint = new Vector3f(rng.nextFloat(maxX), 0, rng.nextFloat(maxY));

        List<Box2TreeElement> shapes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            shapes.add(generateShape(i, basePoint, maxRadius, 1f, 5f));
        }

        return shapes;
    }

    private Box2TreeElement generateShape(int id, Vector3f basePoint, float maxRadius, float minSize, float maxSize) {
        Random rng = RandomProvider.defaultRandom().get();
        Vector3f point = basePoint
            .addLocal(new Vector3f(rng.nextFloat(), 0, rng.nextFloat())
                .normalizeLocal()
                .multLocal(rng.nextFloat(maxRadius))
            );

        float size = rng.nextFloat(minSize, maxSize);

        @Data
        @AllArgsConstructor
        class Element implements Box2TreeElement {
            private int id;
            private Box2f boundingBox;
        }

        return new Element(id, new Box2f(point, size / 2f, size / 2f));
    }

}
