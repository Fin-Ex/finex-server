package ru.finex.core.component.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;
import ru.finex.core.tick.TickService;
import ru.finex.core.tick.TickStage;
import ru.finex.core.utils.GenericUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @param <T> component type
 * @author m0nster.mind
 */
public abstract class AbstractComponentLogicService<T extends Component> {

    private static final SnakeCaseStrategy SNAKE_CASE = new SnakeCaseStrategy();

    protected List<T> components = Collections.emptyList();

    @Inject
    protected TickService tickService;
    @Inject
    protected ComponentService componentService;

    private Class<T> type;

    protected void addComponent(T component) {
        var components = new ArrayList<>(this.components);
        components.add(component);
        this.components = components;
    }

    protected boolean removeComponent(T component) {
        var components = new ArrayList<>(this.components);
        boolean result = components.remove(component);
        this.components = components;

        return result;
    }

    protected T getComponent(GameObject gameObject) {
        if (type == null) {
            type = GenericUtils.getGenericType(getClass(), 0);
        }

        return componentService.getComponent(gameObject, type);
    }

    @PostConstruct
    protected void registerTickStages() {
        Method onTickMethod;
        try {
            onTickMethod = AbstractComponentLogicService.class.getDeclaredMethod("onTick");
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        Class<?> clazz = getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            String name = method.getName();
            if (name.length() < 2) {
                continue;
            }

            // remove "on" and translate to upper snake case
            name = SNAKE_CASE.translate(name.substring(2)).toUpperCase();

            TickStage stage;
            try {
                stage = TickStage.valueOf(name);
            } catch (IllegalArgumentException e) {
                continue;
            }

            tickService.register(this, onTickMethod, stage);
        }
    }

    /**
     * Execute tick on a components.
     * <p>
     * <b>Its method doesn't call manually.</b>
     */
    public final void onTick() {
        TickStage stage = tickService.getTickStage();
        List<T> components = this.components; // hold reference to current components list
        for (int i = 0; i < components.size(); i++) {
            T component = components.get(i);
            switch (stage) {
                case UPDATE -> onUpdate(component);
                case PRE_UPDATE -> onPreUpdate(component);
                case POST_UPDATE -> onPostUpdate(component);
                case PHYSICS -> onPhysics(component);
                case PRE_PHYSICS -> onPrePhysics(component);
                case POST_PHYSICS -> onPostPhysics(component);
                case INPUT -> onInput(component);
                case PRE_INPUT -> onPreInput(component);
                case POST_INPUT -> onPostInput(component);
                default -> throw new RuntimeException("Unknown tick stage: " + stage);
            }
        }
    }

    protected void onPreInput(T component) {
        // virtual
    }

    protected void onInput(T component) {
        // virtual
    }

    protected void onPostInput(T component) {
        // virtual
    }

    protected void onPrePhysics(T component) {
        // virtual
    }

    protected void onPhysics(T component) {
        // virtual
    }

    protected void onPostPhysics(T component) {
        // virtual
    }

    protected void onPreUpdate(T component) {
        // virtual
    }

    protected void onUpdate(T component) {
        // virtual
    }

    protected void onPostUpdate(T component) {
        // virtual
    }

}
