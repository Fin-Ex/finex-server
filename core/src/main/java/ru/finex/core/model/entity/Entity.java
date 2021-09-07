package ru.finex.core.model.entity;

/**
 * Общий интерфейс для всех персистентных сущностей.
 *
 * @author m0nster.mind
 */
public interface Entity {

    /**
     * Получить ID (primary key) под которым идет сохранение сущности в БД.
     *
     * @return ID
     */
    int getPersistenceId();

    /**
     * Установить ID (primary key) сущности.
     *
     * @param persistenceId ID
     */
    void setPersistenceId(int persistenceId);

}
