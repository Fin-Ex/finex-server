package ru.finex.testing.server;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Starts server core with specified parameters.
 * @author m0nster.mind
 */
@ExtendWith(ServerExtension.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Server {

    /**
     * Path to configuration file.
     * @return configuration file path
     */
    String config() default "";

    /**
     * Injector modules for server.
     * @return injector modules
     */
    Class[] modules() default {};

}
