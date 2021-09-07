package ru.finex.core.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Инжектируемые модули.
 * Вторичные модули, в которых возможно использовать инжектирование классов с уровня {@link LoaderModule}.
 *
 * @author m0nster.mind
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InjectedModule {
}
