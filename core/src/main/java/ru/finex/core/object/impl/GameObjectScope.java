package ru.finex.core.object.impl;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import lombok.RequiredArgsConstructor;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Named;

@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectScope implements Scope {

    /**
     * Named scope of game object runtime ID.
     */
    public static final String RUNTIME_ID = "RuntimeID";

    /**
     * Named scope of game object persistence ID.
     */
    public static final String PERSISTENCE_ID = "PersistenceID";

    private final ThreadLocal<ScopeContext> localGameObject = ThreadLocal.withInitial(ScopeContext::new);
    private final Provider<ComponentService> componentServiceProvider;

    /**
     * Enter to game object scope.
     * <p>Store the game object into thread local.
     * @param gameObject the game object
     */
    public void enterScope(GameObject gameObject) {
        localGameObject.get().enterScope(gameObject);
    }

    /**
     * Exit from game object scope.
     * <p>Delete the game object from thread local.
     * @param gameObject the game object
     */
    public void exitScope(GameObject gameObject) {
        localGameObject.get().exitScope(gameObject);
    }

    /**
     * Create new scoped game object provider.
     * @return game object provider
     */
    public Provider<GameObject> gameObjectProvider() {
        return () -> localGameObject.get().getScopedObject();
    }

    /**
     * Create new scoped runtime ID provider.
     * @return runtime ID provider
     */
    public Provider<Integer> runtimeIdProvider() {
        return () -> localGameObject.get().getScopedObject().getRuntimeId();
    }

    /**
     * Create new scoped persistence ID provider.
     * @return persistence ID provider
     */
    public Provider<Integer> persistenceIdProvider() {
        return () -> localGameObject.get().getScopedObject().getPersistenceId();
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
        return () -> {
            GameObject gameObject = localGameObject.get().getScopedObject();
            if (gameObject == null) {
                return provider.get();
            }

            T result = null;
            Annotation annotation = key.getAnnotation();
            Type requiredType = key.getTypeLiteral().getType();
            if (requiredType instanceof Class type) {
                if (GameObject.class.isAssignableFrom(type)) {
                    result = (T) gameObject;
                } else if (Component.class.isAssignableFrom(type)) {
                    ComponentService componentService = componentServiceProvider.get();
                    result = (T) componentService.getComponent(gameObject, type);
                } else if (annotation != null) {
                    if (annotation instanceof Named || annotation instanceof com.google.inject.name.Named) {
                        result = namedScope(type, annotation, gameObject);
                    }
                }
            }

            if (result == null) {
                result = provider.get();
            }

            return result;
        };
    }

    private <T> T namedScope(Class<?> type, Annotation named, GameObject gameObject) {
        String name;
        if (named instanceof Named cdi) {
            name = cdi.value();
        } else if (named instanceof com.google.inject.name.Named guice) {
            name = guice.value();
        } else {
            throw new RuntimeException(String.format(
                    "Unknown named annotation: %s", named.annotationType().getCanonicalName()
            ));
        }

        T result = null;
        if (RUNTIME_ID.equalsIgnoreCase(name)) {
            if (int.class.isAssignableFrom(type)) {
                Object reference = gameObject.getRuntimeId();
                result = (T) reference;
            } else if (Integer.class.isAssignableFrom(type)) {
                result = (T) Integer.valueOf(gameObject.getRuntimeId());
            }
        } else if (PERSISTENCE_ID.equalsIgnoreCase(name)) {
            if (int.class.isAssignableFrom(type)) {
                Object reference = gameObject.getPersistenceId();
                result = (T) reference;
            } else if (Integer.class.isAssignableFrom(type)) {
                result = (T) Integer.valueOf(gameObject.getPersistenceId());
            }
        }

        return result;
    }

}
