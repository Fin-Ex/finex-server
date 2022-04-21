package ru.finex.core.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Query parameter what passed to query.
 * @author m0nster.mind
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {

    /**
     * Parameter name what passed into query.
     * @return parameter name
     */
    String value();

}
