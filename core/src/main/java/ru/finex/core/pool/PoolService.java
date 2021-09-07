package ru.finex.core.pool;

import org.apache.commons.pool2.ObjectPool;

/**
 * @author m0nster.mind
 */
public interface PoolService {

    /**
     * Получить объект из пула.
     *
     * @param type тип объекта
     * @param <T> генерик тип объекта
     * @return объект
     */
    <T> T getObject(Class<T> type);

    /**
     * Вернуть объект в пул.
     *
     * @param object объект
     */
    void returnObject(Object object);

    /**
     * Зарегистрировать пул объектов в сервисе.
     *
     * @param type тип объектов в пуле
     * @param pool пул
     */
    void registerPool(Class<?> type, ObjectPool<?> pool);

}
