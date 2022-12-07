package ru.finex.core.inject;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mycila.guice.ext.closeable.CloseableModule;
import com.mycila.guice.ext.jsr250.Jsr250Module;
import com.mycila.jmx.JmxModule;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import ru.finex.core.GlobalContext;
import ru.finex.core.ServerApplication;
import ru.finex.core.utils.InjectorUtils;
import ru.vyarus.guice.validator.ValidationModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import static ru.finex.core.ServerApplication.MODULES_ARG;

/**
 * @author m0nster.mind
 */
@Slf4j
public class ModuleService {

    private Node[] nodes;
    private int[] roots;

    /**
     * Build a modules tree.
     */
    public void buildTree() {
        nodes = collectNodes();
        bindNodes(nodes);
        roots = findRoots(nodes);
    }

    /**
     * Find root modules to inject.
     * @return modules
     */
    public List<Module> findInjectableModules() {
        return findInjectableModules(roots);
    }

    private List<Module> findInjectableModules(int[] roots) {
        Queue<Integer> awaits = new LinkedList<>();
        for (int i = 0; i < roots.length; i++) {
            awaits.add(roots[i]);
        }

        List<Module> modules = new ArrayList<>();
        for (Integer index = awaits.poll(); index != null; index = awaits.poll()) {
            Node node = nodes[index];

            if (node.childs.length > 0) {
                modules.add(Modules.override(node.source).with(findInjectableModules(node.childs)));
            } else {
                modules.add(node.source);
            }
        }

        return modules;
    }

    private static Node[] collectNodes() {
        HashSet<Node> modules = new HashSet<>();
        modules.add(new Node(new CloseableModule()));
        modules.add(new Node(new Jsr250Module()));
        modules.add(new Node(new JmxModule()));
        modules.add(new Node(new ValidationModule()));
        modules.addAll(InjectorUtils.collectModules(ServerApplication.class.getPackageName(), LoaderModule.class)
            .stream()
            .map(Node::new)
            .collect(Collectors.toList())
        );
        modules.addAll(InjectorUtils.collectModules(GlobalContext.rootPackage, LoaderModule.class)
            .stream()
            .map(Node::new)
            .collect(Collectors.toList())
        );
        Optional.ofNullable(GlobalContext.arguments.get(MODULES_ARG))
            .map(e -> e.split("[,;]"))
            .map(InjectorUtils::collectModules)
            .map(list -> list.stream()
                .map(Node::new)
                .collect(Collectors.toList())
            ).ifPresent(modules::addAll);

        return modules.toArray(new Node[0]);
    }

    private static void bindNodes(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            Module source = node.source;
            Class<? extends Module> sourceClass = source.getClass();
            OverrideModule meta = sourceClass.getDeclaredAnnotation(OverrideModule.class);
            if (meta == null) {
                continue;
            }

            int parentIndex = findBySourceType(nodes, meta.value());
            if (parentIndex == -1) {
                log.warn("Parent module '{}' not found for module '{}'", meta.value().getCanonicalName(), sourceClass.getCanonicalName());
                continue;
            }

            Node parent = nodes[parentIndex];
            parent.childs = ArrayUtils.add(parent.childs, i);
            node.parent = parentIndex;
        }
    }

    private static int[] findRoots(Node[] nodes) {
        IntList roots = new IntArrayList();
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (node.parent == -1) {
                roots.add(i);
            }
        }

        return roots.toIntArray();
    }

    private static int findBySourceType(Node[] nodes, Class<? extends Module> sourceType) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (node.source.getClass() == sourceType) {
                return i;
            }
        }

        return -1;
    }

    private static class Node {
        private Module source;
        private int parent = -1;
        private int[] childs = ArrayUtils.EMPTY_INT_ARRAY;

        Node(Module source) {
            this.source = Objects.requireNonNull(source);
        }
    }

}
