package ru.finex.testing.server;

import lombok.Data;

/**
 * @author m0nster.mind
 */
@Data
public class ServerModule {

    private final String canonicalPath;

    public ServerModule(Class<? extends com.google.inject.Module> moduleType) {
        canonicalPath = moduleType.getCanonicalName();
    }

    /**
     * Create new server module.
     * @param moduleType guice module class
     * @return server module
     */
    public static ServerModule of(Class<? extends com.google.inject.Module> moduleType) {
        return new ServerModule(moduleType);
    }

}
