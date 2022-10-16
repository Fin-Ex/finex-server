package ru.finex.core.persistence;

import ru.finex.core.object.GameObject;

/**
 * Сервис для управления персистенцией игрового объекта.
 *
 * @author m0nster.mind
 */
public interface GameObjectPersistenceService {

    /**
     * Сохранить игровой объект в БД.
     *
     * @param gameObject игровой объект
     */
    void persist(GameObject gameObject);

    /**
     * Восстанавливает игровой объект из БД.
     *
     * @param gameObject игровой объект
     */
    void restore(GameObject gameObject);

}
