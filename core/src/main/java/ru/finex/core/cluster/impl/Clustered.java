package ru.finex.core.cluster.impl;

import org.redisson.api.RObject;

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

    /**
     * Name of object.
     * <p>
     * If empty name will generate from field name or method name.
     * <p>
     * Supports expressions.
     * @return name of object
     * @see ru.finex.core.cluster.ClusterService#getName(Class, String)
     * @see ru.finex.core.cluster.ClusterService#getName(Class, String, String)
     * @see ru.finex.core.placeholder.PlaceholderService
     */
    String value() default "";

    /**
     * Determines what clustered object is automatically managed (registered as managed resource).
     * @return auto management
     * @see ru.finex.core.cluster.ClusterService#registerManagedResource(RObject)
     */
    boolean autoManagement() default true;

}
