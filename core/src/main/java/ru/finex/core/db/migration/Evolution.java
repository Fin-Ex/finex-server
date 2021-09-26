package ru.finex.core.db.migration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Регистрирует в сервисе миграций выполнение эволюций указанного типа.
 *
 * @author m0nster.mind
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Evolution {

    /**
     * Тип эволюции (компонент).
     *
     * @return тип эволюции
     */
    String value();

    /**
     * Зависимости от других компонентов.
     * @return зависимости
     */
    String[] dependencies() default "core";

}
