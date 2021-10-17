package ru.finex.core.model.entity;

import java.io.Serializable;

/**
 * Общий интерфейс для всех персистентных сущностей.
 *
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:JavadocType")
public interface Entity<ID extends Serializable> {

    /**
     * Получить ID (primary key) под которым идет сохранение сущности в БД.
     *
     * @return ID
     */
    ID getPersistenceId();

    /**
     * Установить ID (primary key) сущности.
     *
     * @param persistenceId ID
     */
    void setPersistenceId(ID persistenceId);

}
