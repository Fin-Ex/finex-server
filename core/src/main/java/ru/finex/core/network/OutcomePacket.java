package ru.finex.core.network;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для исходящих пакетов от сервера.
 *
 * @author m0nster.mind
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OutcomePacket {

    /** Опкоды пакета. */
    Opcode[] value();

}
