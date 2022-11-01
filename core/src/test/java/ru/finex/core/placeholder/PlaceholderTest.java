package ru.finex.core.placeholder;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.testing.server.Server;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Server(config = "placeholder-test.conf", modules = {
        HoconModule.class,
        PlaceholderModule.class
})
public class PlaceholderTest {

    @Inject
    private PlaceholderService service;

    @Test
    public void configExists() {
        String value = service.evaluate("${config}", String.class);
        Assertions.assertTrue(!StringUtils.isBlank(value));
    }

    @Test
    public void configExpression() {
        int value = service.evaluate("${config['path.of.Value.theValue']}", int.class);
        Assertions.assertEquals(4444, value);
    }

    @Test
    public void configJoinExpression() {
        String value = service.evaluate("text ${config['path.of.Value.theValue']}", String.class);
        Assertions.assertEquals("text 4444", value);
    }

    @Test
    public void callerContextBoxing() {
        TestCallerBox caller = new TestCallerBox(new TestCaller("And inside my fragile me lies the truth concealed"));
        String value = service.evaluate("${caller.getTheValue()}", caller, String.class);
        Assertions.assertEquals(caller.caller.theValue, value);
    }

    @Data
    @AllArgsConstructor
    public class TestCaller {
        private String theValue;
    }

    @Data
    @AllArgsConstructor
    public class TestCallerBox {
        private TestCaller caller;
    }

}
