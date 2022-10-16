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
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectScope implements Scope {

    private final ThreadLocal<GameObject> localGameObject = new ThreadLocal<>();
    private final ComponentService componentService;

    /**
     * Enter to game object scope.
     * <p>Store the game object into thread local.
     * @param gameObject the game object
     */
    public void enterScope(GameObject gameObject) {
        if (localGameObject.get() != null) {
            throw new RuntimeException("Already in GameObject scope!");
        }
        localGameObject.set(gameObject);
    }

    /**
     * Exit from game object scope.
     * Delete the game object from thread local.
     */
    public void exitScope() {
        localGameObject.remove();
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
        return () -> {
            GameObject gameObject = localGameObject.get();
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
        if ("RuntimeID".equalsIgnoreCase(name)) {
            if (int.class.isAssignableFrom(type)) {
                Object reference = gameObject.getRuntimeId();
                result = (T) reference;
            } else if (Integer.class.isAssignableFrom(type)) {
                result = (T) Integer.valueOf(gameObject.getRuntimeId());
            }
        } else if ("PersistenceID".equalsIgnoreCase(name)) {
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
