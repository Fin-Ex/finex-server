package ru.finex.core.persistence;

/**
 * Интерфейс сохраняемого объекта.
 *
 * @author m0nster.mind
 */
public interface PersistenceObject {

    /**
     * ID сохраняемого объекта, с которым работает БД.
     *
     * @return persistence id
     */
    int getPersistenceId();

}
