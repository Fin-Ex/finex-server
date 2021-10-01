package ru.finex.core;

import com.google.inject.Injector;

/**
 * @author m0nster.mind
 */
@Deprecated(forRemoval = true)
public interface ServerContext {

    Injector getInjector();
    void setInjector(Injector injector);

}
