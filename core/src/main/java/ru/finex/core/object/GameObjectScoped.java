package ru.finex.core.object;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация указывающая, что объект входит в скоуп игрового объекта.
 * <p>Если аннотация указана на супер-классе, то все наследники также будут
 *  автоматически помечены ей.
 * @author m0nster.mind
 */
@Inherited
@Documented
@ScopeAnnotation
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GameObjectScoped {
}
