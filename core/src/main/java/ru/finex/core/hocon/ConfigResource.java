package ru.finex.core.hocon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * Аннотация, которая позволяет инжектировать значение из конфига.
 *
 * Аннотацию нельзя размещать на параметры конструктора, ввиду технических ограничений Guice.
 *
 * @author m0nster.mind
 */
@Qualifier
@Documented
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigResource {

    /**
     * Имя переменной из конфигурации.
     * По умолчанию: имя переменной - если аннотация указана на классе
     *
     * @return имя переменной.
     */
    String value() default "";

    /**
     * Базовое имя.
     * Путь до переменной внутри конфигурации.
     * По умолчанию: package.class-name - если аннотация указана на классе
     *
     * @return базовое имя
     */
    String basePath() default "";

    /**
     * Может ли данное значение отсутствовать в конфигурации.
     * Если значение отсутствует на момент инжектирования в поле, то
     *  будет выброшено исключение.
     *
     * @return может ли значение отсутствовать к конфигурации
     */
    boolean canMissing() default false;

    /**
     * Может ли данное значение иметь null в конфигурации.
     * Если значение не может иметь null, тогда будет выброшено исключение при инжектировании поля,
     *  если в конфигурации выставлен null.
     *
     * @return может ли данное значение иметь null в конфигурации
     */
    boolean nullable() default false;

}
