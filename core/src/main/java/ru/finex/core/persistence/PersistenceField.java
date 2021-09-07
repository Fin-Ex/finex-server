package ru.finex.core.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, которая указывает нужный тип персистент сервиса, для сохранения поля в БД.
 *
 * @author m0nster.mind
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PersistenceField {

    Class<? extends PersistenceService> value();

}
