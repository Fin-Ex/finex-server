package ru.finex.core.model.object.scope.impl;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import lombok.RequiredArgsConstructor;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.object.scope.GameObjectScope;
import ru.finex.core.object.GameObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author oracle
 */
@RequiredArgsConstructor
public class DefaultGameObjectScope implements Scope, GameObjectScope<GameObject> {

    private static final ThreadLocal<Queue<GameObject>> CTX = ThreadLocal.withInitial(LinkedList::new);

    private final Provider<ComponentService> componentServiceProvider;

    @Override
    public void enterScope(GameObject context) {
        Queue<GameObject> queue = CTX.get();
        queue.offer(context);
    }

    @Override
    public void exitScope() {
        Queue<GameObject> queue = CTX.get();
        queue.poll();
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> {
            GameObject gameObject = CTX.get().peek();
            if (gameObject == null) {
                throw new RuntimeException("Unknown GameObject scope!");
            }

            Object result;
            Type requiredType = key.getTypeLiteral().getType();
            if (requiredType instanceof Class clazz) {
                result = getScoped(gameObject, clazz);
                if (result == null) {
                    result = createScoped(gameObject, clazz, unscoped);
                }
            } else {
                result = unscoped.get();
            }

            return (T) result;
        };
    }

    private Object getScoped(GameObject gameObject, Class clazz) {
        Object result = null;
        if (GameObject.class.isAssignableFrom(clazz)) {
            result = gameObject;
        } else if (Component.class.isAssignableFrom(clazz)) {
            ComponentService componentService = componentServiceProvider.get();
            result = componentService.getComponent(gameObject, clazz);
        }

        return result;
    }

    private Object createScoped(GameObject gameObject, Class clazz, Provider provider) {
        // TODO m0nster.mind: если тип компонент, тогда его необходимо создать через сервис
        return provider.get();
    }

}
