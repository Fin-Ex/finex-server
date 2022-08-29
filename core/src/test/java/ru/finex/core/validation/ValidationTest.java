package ru.finex.core.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.junit.ClassRule;
import org.junit.Test;
import ru.finex.core.ContainerRule;
import ru.finex.core.ContainerRule.Type;
import ru.finex.core.GlobalContext;
import ru.finex.core.ServerRule;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
public class ValidationTest {

    @ClassRule(order = 0)
    public static ContainerRule containers = new ContainerRule(Type.Database);

    @ClassRule(order = 1)
    public static ServerRule server = ServerRule.builder()
        .configPath("database-test.conf")
        .configModule()
        .databaseModule()
        .build();


    @Test(expected = ConstraintViolationException.class)
    public void failConstraintTest() {
        SomeService service = GlobalContext.injector.getInstance(SomeService.class);
        service.rangeValue("5");
    }

    @Test
    public void successConstraintTest() {
        SomeService service = GlobalContext.injector.getInstance(SomeService.class);
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
