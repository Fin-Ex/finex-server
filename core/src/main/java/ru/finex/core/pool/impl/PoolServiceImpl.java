package ru.finex.core.pool.impl;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import ru.finex.core.GlobalContext;
import ru.finex.core.placeholder.PlaceholderService;
import ru.finex.core.pool.PoolConfiguration;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.PooledObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PoolServiceImpl implements PoolService {

    private final Map<Class<?>, ObjectPool> pools = new HashMap<>();
    private final PlaceholderService placeholderService;

    @Inject
    public PoolServiceImpl(PlaceholderService placeholderService) {
        this.placeholderService = placeholderService;

        GlobalContext.reflections.getTypesAnnotatedWith(PoolConfiguration.class)
            .stream()
            .map(type -> GlobalContext.injector.getInstance(type))
            .forEach(this::registerPool);

        GlobalContext.reflections.getTypesAnnotatedWith(PooledObject.class)
            .forEach(this::createDynamicPool);
    }

    @Override
    public void registerPool(Class<?> type, ObjectPool<?> pool) {
        pools.put(type, pool);
    }

    private void registerPool(Object poolConfiguration) {
        MethodUtils.getMethodsListWithAnnotation(poolConfiguration.getClass(), PoolConfiguration.class)
            .stream()
            .map(method -> getPoolFromConfiguration(poolConfiguration, method))
            .forEach(pair -> registerPool(pair.getLeft(), pair.getRight()));
    }

    private Pair<Class<?>, ObjectPool<?>> getPoolFromConfiguration(Object poolConfiguration, Method method) {
        Pair<Class<?>, ObjectPool<?>> typeAndPool;
        try {
            if (Pair.class.isAssignableFrom(method.getReturnType())) {
                typeAndPool = (Pair<Class<?>, ObjectPool<?>>) method.invoke(Modifier.isStatic(method.getModifiers()) ? null : poolConfiguration);
            } else {
                ObjectPool<?> pool = (ObjectPool<?>) method.invoke(Modifier.isStatic(method.getModifiers()) ? null : poolConfiguration);
                Class<?> type = getPoolType(method);
                typeAndPool = Pair.of(type, pool);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return typeAndPool;
    }

    @Override
    public void unregisterPool(Class<?> type) {
        pools.remove(type);
    }

    @Override
    public <T> ObjectPool<T> createDynamicPool(Class<T> pooledObjectType, PooledObject pooledObject) {
        PooledObjectFactory<?> pooledObjectFactory;
        if (pooledObject.factory() == SimplePooledObjectFactory.class) {
            pooledObjectFactory = new SimplePooledObjectFactory(pooledObjectType);
        } else {
            pooledObjectFactory = GlobalContext.injector.getInstance(pooledObject.factory());
        }

        int maxSize = placeholderService.evaluate(pooledObject.maxSize(), int.class);
        int minSize = placeholderService.evaluate(pooledObject.minSize(), int.class);
        ArrayDequePool<?> pool = new ArrayDequePool(pooledObjectFactory, maxSize);
        try {
            for (int i = 0; i < minSize; i++) {
                pool.addObject();
            }
        } catch (Exception e) {
            throw new RuntimeException("Fail to preheating pool for: " + pooledObjectType.getCanonicalName() + " with " + minSize + " elements!");
        }

        registerPool(pooledObjectType, pool);
        return (ObjectPool<T>) pool;
    }

    private <T> ObjectPool<T> createDynamicPool(Class<T> pooledObjectType) {
        PooledObject poolConfiguration = pooledObjectType.getDeclaredAnnotation(PooledObject.class);
        return createDynamicPool(pooledObjectType, poolConfiguration);
    }

    private Class<?> getPoolType(Method method) {
        ParameterizedType returnType = (ParameterizedType) method.getGenericReturnType();
        return (Class<?>) returnType.getActualTypeArguments()[0];
    }

    @Override
    public <T> T getObject(Class<T> type) {
        ObjectPool<T> pool = pools.get(type);
        Objects.requireNonNull(pool, () -> String.format("Pool with '%s' type not found!", type.getCanonicalName()));

        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnObject(Object object) {
        ObjectPool pool = pools.get(object.getClass());
        Objects.requireNonNull(pool, () -> String.format("Pool with '%s' type not found!", object.getClass().getCanonicalName()));

        try {
            pool.returnObject(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
