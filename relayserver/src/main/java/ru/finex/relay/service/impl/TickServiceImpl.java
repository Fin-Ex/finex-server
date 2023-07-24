package ru.finex.relay.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import ru.finex.core.tick.TickService;
import ru.finex.core.tick.TickStage;

import java.lang.reflect.Method;
import javax.inject.Singleton;

/**
 * Just stub.
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@NoArgsConstructor
public class TickServiceImpl implements TickService {

    @Override
    public void register(Object instance, Method method, TickStage stage) {
        throw new NotImplementedException();
    }

    @Override
    public void tick() {
        throw new NotImplementedException();
    }

    @Override
    public float getDeltaTime() {
        return 0;
    }

    @Override
    public long getDeltaTimeMillis() {
        return 0;
    }

    @Override
    public TickStage getTickStage() {
        return TickStage.INPUT;
    }

}
