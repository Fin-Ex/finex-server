package ru.finex.core.cluster.impl;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * Автоматически инжектирует распределенные объекты.
 * Аннотацию нельзя размещать на конструктор, ввиду технических ограничений Guice.
 *
 * @author m0nster.mind
 */
@Qualifier
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Clustered {

    String value() default "";

}
