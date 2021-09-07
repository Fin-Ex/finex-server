package ru.finex.core.pool;

import org.apache.commons.pool2.ObjectPool;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, которая указывает, что класс является конфигуратором пула.
 * Все методы отмеченные аннотацией будут зарегистрированы в {@link PoolService#registerPool(Class, ObjectPool)}.
 * Для корректной работы аннотацией также необходимо отметить класс.
 *
 * @author m0nster.mind
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PoolConfiguration {
}
