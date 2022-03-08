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

    /**
     * Удалить регистрацию пула.
     *
     * @param type тип объекта хранимого в пуле
     */
    void unregisterPool(Class<?> type);

    /**
     * Создать динамический пул и зарегистрировать его.
     * Если пул с данным типом объекта уже зарегистрирован, то новый пул заменит предыдущий.
     *
     * @param pooledObjectType тип объекта хранимого в пуле
     * @param pooledObject конфигурация пула
     * @param <T> генерик тип объекта хранимого в пуле
     * @return пул
     * @see #registerPool(Class, ObjectPool)
     */
    <T> ObjectPool<T> createDynamicPool(Class<T> pooledObjectType, PooledObject pooledObject);

}
