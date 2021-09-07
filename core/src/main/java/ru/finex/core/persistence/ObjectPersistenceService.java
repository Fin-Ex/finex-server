package ru.finex.core.persistence;

import ru.finex.core.persistence.PersistenceObject;

/**
 * Сервис для управления персистенцией.
 *
 * @author m0nster.mind
 */
public interface ObjectPersistenceService {

    /**
     * Сохранить объект в БД.
     *
     * @param object объект
     */
    void persist(PersistenceObject object);

    /**
     * Восстановить объект из БД.
     *
     * @param object объект
     */
    void restore(PersistenceObject object);

}
