package ru.finex.core.model.object.scope.impl;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import ru.finex.core.model.object.GameObject;
import ru.finex.core.model.object.scope.GameObjectScope;

import java.lang.reflect.Type;

/**
 * @author oracle
 */
public class DefaultGameObjectScope implements Scope, GameObjectScope<GameObject> {

    private static final ThreadLocal<GameObject> CTX = new ThreadLocal<>();

    @Override
    public void enterScope(GameObject context) {
        CTX.set(context);
    }

    @Override
    public void exitScope() {
        CTX.remove();
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> {
            GameObject contextual = CTX.get();

            if (contextual == null) {
                return unscoped.get();
            }

            Object result = null;
            Type requiredType = key.getTypeLiteral().getType();
            if(requiredType instanceof Class clazz) {
                if(GameObject.class.isAssignableFrom(clazz)) {
                    result = contextual;
                }
            }

            if(result == null) {
                result = unscoped.get();
                if(!Scopes.isCircularProxy(key)) {
                    if(result instanceof GameObject resultGo) {
                        CTX.set(resultGo);
                    }
                }
            }

            return (T) result;
        };
    }

}
