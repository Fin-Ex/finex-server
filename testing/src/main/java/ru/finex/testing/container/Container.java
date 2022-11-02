package ru.finex.testing.container;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Starts before all tests specified containers and down its after all tests.
 * @author m0nster.mind
 */
@ExtendWith(ContainerExtension.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Container {

    ContainerType[] value();

}
