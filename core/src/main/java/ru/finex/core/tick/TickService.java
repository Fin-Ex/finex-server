package ru.finex.core.tick;

import java.lang.reflect.Method;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public interface TickService {

    void register(Object instance, Method method, TickStage stage);

    void tick();

    float getDeltaTime();

    long getDeltaTimeMillis();

    TickStage getTickStage();

}
