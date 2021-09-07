package ru.finex.core;

import com.google.inject.Injector;

/**
 * @author m0nster.mind
 */
public interface ServerContext {

    Injector getInjector();
    void setInjector(Injector injector);

}
