package ru.finex.gs.cluster;

import ru.finex.core.cluster.ServerRole;

import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class ServerRoleProvider implements Provider<ServerRole> {

    private final ServerRole serverRole = new ServerRole("WORLD");

    @Override
    public ServerRole get() {
        return serverRole;
    }

}
