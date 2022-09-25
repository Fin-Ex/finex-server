package ru.finex.core.placeholder;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.finex.core.GlobalContext;
import ru.finex.core.ServerRule;
import ru.finex.core.ServerRule.Module;

/**
 * @author m0nster.mind
 */
public class PlaceholderTest {

    @ClassRule(order = 0)
    public static ServerRule server = ServerRule.builder()
        .configPath("placeholder-test.conf")
        .configModule()
        .module(Module.of(PlaceholderModule.class))
        .build();

    @Test
    public void configExists() {
        PlaceholderService service = GlobalContext.injector.getInstance(PlaceholderService.class);

        String value = service.evaluate("${config}", String.class);
        Assertions.assertTrue(!StringUtils.isBlank(value));
    }

    @Test
    public void configExpression() {
        PlaceholderService service = GlobalContext.injector.getInstance(PlaceholderService.class);

        int value = service.evaluate("${config['path.of.Value.theValue']}", int.class);
        Assertions.assertEquals(4444, value);
    }

    @Test
    public void configJoinExpression() {
        PlaceholderService service = GlobalContext.injector.getInstance(PlaceholderService.class);

        String value = service.evaluate("text ${config['path.of.Value.theValue']}", String.class);
        Assertions.assertEquals("text 4444", value);
    }

    @Test
    public void callerContext() {
        PlaceholderService service = GlobalContext.injector.getInstance(PlaceholderService.class);

        TestCaller caller = new TestCaller("And in myself I feel that Im always outside the reality");
        String value = service.evaluate("${theValue}", caller, String.class);
        Assertions.assertEquals(caller.theValue, value);
    }

    @AllArgsConstructor
    public class TestCaller {
        private String theValue;
    }

}
