package ru.finex.core.tick;

import java.lang.reflect.Method;

/**
 * @author m0nster.mind
 */
public interface TickService {

    /**
     * Register method as tick consumer with specified tick stage.
     * <p>
     * Cannot call package-default and protected methods from same class package,
     *  because java require same class loader with runtime-generated class and bootstrap class.
     * @param instance instance where calls method
     * @param method method to call
     * @param stage tick stage
     * @see RegisterTick
     */
    void register(Object instance, Method method, TickStage stage);

    /**
     * Call all tick consumers ordered by tick stage.
     */
    void tick();

    /**
     * Tick duration in seconds.
     * @return delta time in seconds
     */
    float getDeltaTime();

    /**
     * Tick duration in millis.
     * @return delta time in millis
     */
    long getDeltaTimeMillis();

    /**
     * Current tick stage.
     * @return tick stage
     */
    TickStage getTickStage();

}
