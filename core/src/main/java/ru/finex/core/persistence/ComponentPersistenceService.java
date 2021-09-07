package ru.finex.core.persistence;

import ru.finex.core.component.Component;

/**
 * Сервис для управления персистенцией компонентов.
 *
 * @author m0nster.mind
 */
public interface ComponentPersistenceService extends ObjectPersistenceService {

    /**
     * Сохранить компонент в БД.
     *
     * @param component компонент
     */
    void persist(Component component);

    /**
     * Восстановить компонент из БД.
     *
     * @param component компонент
     */
    void restore(Component component);

}
