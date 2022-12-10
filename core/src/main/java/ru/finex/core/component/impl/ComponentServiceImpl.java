package ru.finex.core.component.impl;

import com.google.inject.Injector;
import lombok.RequiredArgsConstructor;
import ru.finex.core.GlobalContext;
import ru.finex.core.cluster.impl.Clustered;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.events.cluster.ClusterEventBus;
import ru.finex.core.model.event.GameObjectEvent;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.impl.GameObjectScope;
import ru.finex.core.pool.PoolService;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.core.prototype.GameObjectPrototypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ComponentServiceImpl implements ComponentService {

    private final Map<Integer, GameObjectComponents> goComponents = new HashMap<>();
    private final Map<Class<? extends Component>, ArrayList<Component>> components = new HashMap<>();
    private final PoolService poolService;
    private final GameObjectPrototypeService prototypeService;
    private final GameObjectScope gameObjectScope;

    @Clustered(GameObjectEvent.CHANNEL)
    private ClusterEventBus<GameObjectEvent> eventBus;

    @Inject
    @Named("ComponentMappers")
    private Map<Class<? extends ComponentPrototype>, ComponentPrototypeMapper> mappers;

    @Override
    public void addComponentsFromPrototype(String prototypeName, GameObject gameObject) {
        prototypeService.getPrototypesByName(prototypeName)
            .forEach(prototype -> addComponent(gameObject, prototype));
    }

    //@Override
    /**
     * Добавляет игровому объекту компонент, создавая его из прототипа.
     * @param gameObject игровой объект, которому будет добавлен компонент
     * @param prototype прототип компонента
     */
    public void addComponent(GameObject gameObject, ComponentPrototype prototype) {
        ComponentPrototypeMapper mapper = mappers.get(prototype.getClass());
        Component component = mapper.map(prototype);
        addComponent(gameObject, component);
    }

    @Override
    public void addComponent(GameObject gameObject, Component component) {
        if (component.getGameObject() != null) {
            throw new RuntimeException(String.format("Trying attach component to %s, component already attached to %s game object.",
                gameObject.toString(),
                component.getGameObject().toString())
            );
        }

        GameObjectComponents gameObjectComponents = goComponents.computeIfAbsent(gameObject.getRuntimeId(), GameObjectComponents::new);
        Injector injector = GlobalContext.injector;
        ArrayList<Component> components = gameObjectComponents.getComponents();

        gameObjectScope.enterScope(gameObject);
        try {
            injector.injectMembers(component);
            components.add(component);

        } finally {
            gameObjectScope.exitScope(gameObject);
        }

        notifyOnAttachComponent(gameObject, component);
    }

    @Override
    public <T extends Component> T addComponent(GameObject gameObject, Class<T> componentType) {
        GameObjectComponents gameObjectComponents = goComponents.computeIfAbsent(gameObject.getRuntimeId(), GameObjectComponents::new);
        Injector injector = GlobalContext.injector;
        ArrayList<Component> goComponents = gameObjectComponents.getComponents();
        ArrayList<Component> components = this.components.computeIfAbsent(componentType, e -> new ArrayList<>());

        T component;
        gameObjectScope.enterScope(gameObject);
        try {
            component = injector.getInstance(componentType);
            goComponents.add(component);
            components.add(component);
        } finally {
            gameObjectScope.exitScope(gameObject);
        }

        notifyOnAttachComponent(gameObject, component);

        return (T) component;
    }

    private void notifyOnAttachComponent(GameObject gameObject, Component component) {
//        OnComponentAttached event = poolService.getObject(OnComponentAttached.class);
//        event.setGameObject(gameObject);
//        event.setComponent(component);
//        eventBus.notify(event);
        // m0nster.mind: didnt return object to pool, event bus notify is async
        //poolService.returnObject(event);
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    public boolean removeComponent(Component component) {
        GameObject gameObject = component.getGameObject();
        if (gameObject == null) {
            return false;
        }

        int runtimeId = gameObject.getRuntimeId();
        GameObjectComponents gameObjectComponents = goComponents.get(runtimeId);
        if (gameObjectComponents == null) {
            return false;
        }

        ArrayList<Component> components = this.components.get(component.getClass());
        if (components != null) {
            components.remove(component);
        }

        ArrayList<Component> goComponents = gameObjectComponents.getComponents();
        if (!goComponents.remove(component)) {
            return false;
        }

        component.setGameObject(null);

        if (goComponents.isEmpty()) {
            this.goComponents.remove(runtimeId);
        }

        notifyOnDeattachComponent(gameObject, component);

        return true;
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    public boolean removeComponent(GameObject gameObject, Class<? extends Component> componentType) {
        int runtimeId = gameObject.getRuntimeId();
        GameObjectComponents gameObjectComponents = goComponents.get(runtimeId);
        if (gameObjectComponents == null) {
            return false;
        }

        ArrayList<Component> goComponents = gameObjectComponents.getComponents();
        Component component = getComponent(goComponents, componentType);
        if (component == null) {
            return false;
        }

        ArrayList<Component> components = this.components.get(component.getClass());
        if (components != null) {
            components.remove(component);
        }

        goComponents.remove(component);
        component.setGameObject(null);

        if (goComponents.isEmpty()) {
            this.goComponents.remove(runtimeId);
        }

        notifyOnDeattachComponent(gameObject, component);

        return true;
    }

    private void notifyOnDeattachComponent(GameObject gameObject, Component component) {
//        OnComponentDeattached event = poolService.getObject(OnComponentDeattached.class);
//        event.setGameObject(gameObject);
//        event.setComponent(component);
//        eventBus.notify(event);
        // m0nster.mind: didnt return object to pool, event bus notify is async
        //poolService.returnObject(event);
    }

    @Override
    public <T extends Component> T getComponent(GameObject gameObject, Class<T> componentType) {
        GameObjectComponents gameObjectComponents = goComponents.get(gameObject.getRuntimeId());
        if (gameObjectComponents == null) {
            return null;
        }

        return (T) getComponent(gameObjectComponents.getComponents(), componentType);
    }

    private Component getComponent(ArrayList<Component> components, Class<? extends Component> componentType) {
        return components.stream()
            .filter(e -> e.isChildOf(componentType))
            .findAny()
            .orElse(null);
    }

    @Override
    public List<Component> getComponents(GameObject gameObject) {
        GameObjectComponents gameObjectComponents = goComponents.get(gameObject.getRuntimeId());
        if (gameObjectComponents == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(gameObjectComponents.getComponents());
    }

    @Override
    public <T extends Component> List<Component> getComponents(Class<T> componentType) {
        return components.get(componentType);
    }
}
