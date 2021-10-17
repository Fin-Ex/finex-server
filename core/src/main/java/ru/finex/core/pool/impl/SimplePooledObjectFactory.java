package ru.finex.core.pool.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.lang.reflect.Constructor;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:JavadocType")
@RequiredArgsConstructor
public class SimplePooledObjectFactory<E> implements PooledObjectFactory<E> {

    private final Constructor<E> constructor;

    public SimplePooledObjectFactory(Class<E> type) {
        try {
            constructor = type.getConstructor();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void activateObject(PooledObject<E> p) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject<E> p) throws Exception {

    }

    @Override
    public PooledObject<E> makeObject() throws Exception {
        return new DefaultPooledObject<>(constructor.newInstance());
    }

    @Override
    public void passivateObject(PooledObject<E> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<E> p) {
        return true;
    }

}
