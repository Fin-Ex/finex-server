package ru.finex.core.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.testing.container.Container;
import ru.finex.testing.container.ContainerType;
import ru.finex.testing.server.Server;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Container(ContainerType.Database)
@Server(config = "database-test.conf", modules = {
        HoconModule.class,
        DbModule.class
})
public class ValidationTest {

    @Inject
    private SomeService service;

    @Test
    public void failConstraintTest() {
        Assertions.assertThrowsExactly(ConstraintViolationException.class, () -> service.rangeValue("5"));
    }

    @Test
    public void successConstraintTest() {
        service.rangeValue("20");
    }

    @Singleton
    public static class SomeService {
        @Range(min = 10, max = 50)
        public int rangeValue(@NotNull String value) {
            return Integer.parseInt(value);
        }
    }

}
