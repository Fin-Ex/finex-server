package ru.finex.core.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * @author m0nster.mind
 */
public class DefaultLogbackConfiguration {

    private final static String PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%magenta(%thread)] %highlight(%-5level) %cyan(%logger{15}): %msg%n";

    public void applyDefault() {
        LoggerContext ctx = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
        ctx.stop();
        ctx.reset();

        Logger logger = ctx.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        logger.addAppender(createStoutAppender(ctx));
    }

    private Appender<ILoggingEvent> createStoutAppender(LoggerContext ctx) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(PATTERN);
        start(encoder, ctx);

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setEncoder(encoder);
        appender.setName("CONSOLE");
        start(appender, ctx);

        return appender;
    }

    private void start(LifeCycle lifeCycle, LoggerContext ctx) {
        if (lifeCycle instanceof ContextAware) {
            ((ContextAware) lifeCycle).setContext(ctx);
        }
        lifeCycle.start();
    }

}
