package ru.finex.core.network;

import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.command.NoOpCommand;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, которая позволяет выполнить команду при определенном событии.
 *
 * @author m0nster.mind
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {

    /** Тип выполняемой команды. */
    Class<? extends AbstractNetworkCommand> value() default NoOpCommand.class;

}
