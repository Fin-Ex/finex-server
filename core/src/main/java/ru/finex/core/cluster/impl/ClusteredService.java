package ru.finex.core.cluster.impl;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * Аннотация, которая помечает сервис кластерным.
 * <p><ul>
 * <li>В случае размещения аннотации на тип, сервис помечается, как серверный, методы которого могут быть
 *  вызваны другими сервисами в кластере.
 * <li>В случае размещения аннотации на поле, в поле инжектируется перехватчик, который направляет все
 *  вызовы на исполнение в кластер (которые будут исполнены на серверном сервисе).
 * </ul><p>
 * Аннотацию нельзя размещать на конструктор, ввиду технических ограничений Guice.
 * <p>
 * Пример использования:
 * <pre>{@code
 *     // transport library:
 *     interface WorldSessionService {
 *         Object getSession();
 *     }
 *
 *     // world server side:
 *     @ClusteredService
 *     class WorldSessionServiceImpl implements WorldSessionService {
 *
 *         @Override
 *         public Object getSession() {
 *             return new Object();
 *         }
 *
 *     }
 *
 *     // auth server side:
 *     class SomeService {
 *
 *         @ClusteredService
 *         private WorldSessionService worldSessionService;
 *
 *         public void doPew() {
 *             worldSessionService.getSession();
 *         }
 *
 *     }
 * }</pre>
 *
 * @author m0nster.mind
 */
@Qualifier
@Documented
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClusteredService {

}
