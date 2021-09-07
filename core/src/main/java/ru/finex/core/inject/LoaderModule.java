package ru.finex.core.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Модули загрузчика сервера.
 * Начальные модули, которые формируют контекст для последующих модулей.
 *
 * @author m0nster.mind
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoaderModule {
}
