package ru.finex.core.pool.impl;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import ru.finex.core.pool.Cleanable;
import ru.finex.core.utils.FixedArrayDeque;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * {@inheritDoc}
 * @author m0nster.mind
 */
public class ArrayDequePool<E> implements ObjectPool<E> {

    private final Deque<E> values;
    private final PooledObjectFactory<E> factory;

    public ArrayDequePool(PooledObjectFactory<E> factory, int maxSize) {
        this.values = maxSize < 1 ? new ArrayDeque<>() : new FixedArrayDeque<>(maxSize);
        this.factory = factory;
    }

    @Override
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
        PooledObject<E> pooledObject = factory.makeObject();
        pooledObject.allocate();
        values.add(pooledObject.getObject());
    }

    @Override
    public E borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
        return values.remove();
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {
        values.clear();
    }

    @Override
    public void close() {
        values.clear();
    }

    @Override
    public int getNumActive() {
        return -1;
    }

    @Override
    public int getNumIdle() {
        return values.size();
    }

    @Override
    public void invalidateObject(E obj) throws Exception {
        if (obj instanceof Cleanable cleanable) {
            cleanable.clear();
        }

        values.add(obj);
    }

    @Override
    public void returnObject(E obj) throws Exception {
        if (obj instanceof Cleanable cleanable) {
            cleanable.clear();
        }

        values.add(obj);
    }
}
