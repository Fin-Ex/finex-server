package ru.finex.core.db.migration.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.migration.Evolution;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class MigrationTree {

    @RequiredArgsConstructor
    private static class Node {
        private final String value;
        private int[] dependencies = new int[0];
        private int[] xref = new int[0];

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return value.equals(node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    private final Node[] nodes;
    private final int[] roots;

    public MigrationTree() {
        List<Evolution> evolutions = GlobalContext.reflections.getTypesAnnotatedWith(Evolution.class)
            .stream()
            .map(type -> type.getAnnotation(Evolution.class))
            .collect(Collectors.toList());

        nodes = evolutions.stream()
            .map(evolution -> new Node(evolution.value()))
            .toArray(Node[]::new);

        roots = evolutions.stream()
            .filter(this::isRoot)
            .map(Evolution::value)
            .mapToInt(this::findIndex)
            .toArray();

        evolutions.stream()
            .filter(e -> !isRoot(e))
            .forEach(this::bindNodeIndices);

        IntStream.range(0, nodes.length)
            .forEach(this::bindNodeReferences);
    }

    private boolean isRoot(Evolution evolution) {
        String[] dependencies = evolution.dependencies();
        if (dependencies == null || dependencies.length == 0) {
            return true;
        }

        return dependencies.length == 1 && dependencies[0].equals(evolution.value());
    }

    private Node findNode(String value) {
        for (Node n : nodes) {
            if (n.value.equals(value)) {
                return n;
            }
        }

        return null;
    }

    private int findIndex(String value) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].value.equals(value)) {
                return i;
            }
        }

        return -1;
    }

    private void bindNodeIndices(Evolution evolution) {
        Node node = findNode(evolution.value());
        node.dependencies = Stream.of(evolution.dependencies())
            .mapToInt(this::findIndex)
            .toArray();
    }

    private void bindNodeReferences(int index) {
        Node masterNode = nodes[index];
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (ArrayUtils.contains(node.dependencies, index)) {
                masterNode.xref = ArrayUtils.add(masterNode.xref, i);
            }
        }
    }

    public void applyOperation(Consumer<String> operation) {
        Queue<Integer> awaitIndices = new LinkedList<>();
        for (int index : roots) {
            awaitIndices.add(index);
        }

        for (Integer index = awaitIndices.poll(); index != null; index = awaitIndices.poll()) {
            Node node = nodes[index];

            for (int xrefIndex : node.xref) {
                awaitIndices.add(xrefIndex);
            }

            operation.accept(node.value);

        }
    }


}
