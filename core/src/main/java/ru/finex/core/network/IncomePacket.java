package ru.finex.core.network;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для входящих пакетов от клиента.
 *
 * @author m0nster.mind
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IncomePacket {

    /** Опкоды пакета. */
    Opcode[] value();

    /** Выполняемые команды. */
    Cmd[] command();

    /** Auto registration packet in {@link ru.finex.core.network.PacketService PacketService}. */
    boolean autoRegister() default true;

}
