package ru.finex.ws.service.impl;

import com.google.inject.Injector;
import lombok.RequiredArgsConstructor;
import ru.finex.core.cluster.impl.Clustered;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.events.cluster.ClusterEventBus;
import ru.finex.core.model.GameObject;
import ru.finex.core.model.GameObjectEvent;
import ru.finex.core.pool.PoolService;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.core.prototype.GameObjectPrototypeService;
import ru.finex.ws.model.GameObjectComponents;
import ru.finex.ws.model.event.component.OnComponentAttached;
import ru.finex.ws.model.event.component.OnComponentDeattached;
import ru.finex.ws.service.GameObjectInjectorService;

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
    private final PoolService poolService;
    private final GameObjectPrototypeService prototypeService;
    private final GameObjectInjectorService injectorService;

    @Clustered(GameObjectEvent.CHANNEL)
    private ClusterEventBus<GameObjectEvent> eventBus;

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
        Injector injector = injectorService.getOrCreate(gameObject);
        ArrayList<Component> components = gameObjectComponents.getComponents();

        component.setGameObject(gameObject);
        injector.injectMembers(component);
        components.add(component);

        notifyOnAttachComponent(gameObject, component);
    }

    @Override
    public void addComponent(GameObject gameObject, Class<? extends Component> componentType) {
        GameObjectComponents gameObjectComponents = goComponents.computeIfAbsent(gameObject.getRuntimeId(), GameObjectComponents::new);
        Injector injector = injectorService.getOrCreate(gameObject);
        ArrayList<Component> components = gameObjectComponents.getComponents();

        Component component = injector.getInstance(componentType);
        component.setGameObject(gameObject);
        components.add(component);

        notifyOnAttachComponent(gameObject, component);
    }

    private void notifyOnAttachComponent(GameObject gameObject, Component component) {
        OnComponentAttached event = poolService.getObject(OnComponentAttached.class);
        event.setGameObject(gameObject);
        event.setComponent(component);
        eventBus.notify(event);
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

        ArrayList<Component> components = gameObjectComponents.getComponents();
        if (!components.remove(component)) {
            return false;
        }

        component.setGameObject(null);

        if (components.isEmpty()) {
            goComponents.remove(runtimeId);
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

        ArrayList<Component> components = gameObjectComponents.getComponents();
        Component component = getComponent(components, componentType);
        if (component == null) {
            return false;
        }

        components.remove(component);
        component.setGameObject(null);

        if (components.isEmpty()) {
            goComponents.remove(runtimeId);
        }

        notifyOnDeattachComponent(gameObject, component);

        return true;
    }

    private void notifyOnDeattachComponent(GameObject gameObject, Component component) {
        OnComponentDeattached event = poolService.getObject(OnComponentDeattached.class);
        event.setGameObject(gameObject);
        event.setComponent(component);
        eventBus.notify(event);
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

}
