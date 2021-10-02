package ru.finex.ws.tick;

import com.google.inject.ImplementedBy;
import ru.finex.ws.tick.impl.TickServiceImpl;

import java.lang.reflect.Method;

/**
 * @author m0nster.mind
 */
@ImplementedBy(TickServiceImpl.class)
public interface TickService {

    void register(Object instance, Method method, TickStage stage);

    void tick();

    float getDeltaTime();
    long getDeltaTimeMillis();

}
