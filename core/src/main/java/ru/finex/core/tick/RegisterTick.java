package ru.finex.core.tick;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * Register method as tick consumer with specified tick stage.
 * <p>
 * Cannot call package-default and protected methods from same class package,
 *  because java require same class loader with runtime-generated class and bootstrap class.
 *
 * @author m0nster.mind
 * @see TickService#register(Object, Method, TickStage)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterTick {

    /**
     * Tick stage.
     * @return tick stage
     */
    TickStage value();

}
