package ru.finex.core.network;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply this implementation classes when you want one instance per command.
 * @author m0nster.mind
 */
@Documented
@ScopeAnnotation
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NetworkCommandScoped {
}
