package ru.finex.core.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies query to database that method use.
 * @author m0nster.mind
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * Query to database.
     * @return query
     */
    String value();

    /**
     * Determines native query (SQL) or not (JPQL).
     * By default false.
     * @return if true query is SQL, otherwise false (JPQL)
     */
    boolean isNative() default false;

}
