package ru.finex.core;

import com.google.inject.Injector;

/**
 * @author m0nster.mind
 * @deprecated https://github.com/zcxv/finex-server/issues/7
 */
@Deprecated(forRemoval = true)
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public interface ServerContext {

    Injector getInjector();

    void setInjector(Injector injector);

}
