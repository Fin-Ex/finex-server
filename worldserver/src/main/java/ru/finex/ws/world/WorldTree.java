package ru.finex.ws.world;

import ru.finex.core.math.shape.impl.Box2;
import ru.finex.core.model.GameObject;

import java.util.Collection;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:all")
public class WorldTree {

    private static class Node {
        public Box2 box;
        public int value;
        public int next;

        public void free() {
            box = null;
            value = next = 0;
        }
    }

    private static final int PRECISION = 100;

    private Node[] nodes = new Node[0];
    private int count;
    private int leafs;

    public void build(Collection<GameObject> objects) {

    }

    private void clear() {
        count = leafs = 0;
    }

    private void resize(int size) {
        if (size < nodes.length) {
            return;
        }

        final Node[] nodes = new Node[size];
        System.arraycopy(this.nodes, 0, nodes, 0, this.nodes.length);
        this.nodes = nodes;
    }

}
