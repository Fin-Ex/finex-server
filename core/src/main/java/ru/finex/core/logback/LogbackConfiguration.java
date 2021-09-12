package ru.finex.core.logback;

import java.net.URL;

/**
 * @author m0nster.mind
 */
public class LogbackConfiguration {

    private final DefaultLogbackConfiguration configuration = new DefaultLogbackConfiguration();

    public void configureLogback() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource("logback.xml");
        if (resourceUrl == null) {
            configuration.applyDefault();
        }
    }

}
