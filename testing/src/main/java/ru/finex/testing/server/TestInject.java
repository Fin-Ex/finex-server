package ru.finex.testing.server;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injecting bean into parameter.
 * <p>
 * Like {@link javax.inject.Inject Inject} inject bean but from test runner. Its necessary in cases
 *  where injector should not call the method. For example in mixed method parameters where
 *  part of the context may be from other extension.
 * <p>
 * Example:
 * <pre>{@code
 *   @Test
 *   public void testSome(@TestInject Config config, @Mock GameObject gameObject) {
 *       // 1. config - the real object from server injector
 *       // 2. gameObject - mock
 *   }
 * }</pre>
 *
 * @author m0nster.mind
 */
@ExtendWith(ServerExtension.class)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestInject {
}
