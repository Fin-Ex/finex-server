package ru.finex.core.db;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import ru.finex.core.service.DbSessionService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class HibernateSessionProvider implements Provider<Session> {

    private final DbSessionService sessionService;

    @Override
    public Session get() {
        return sessionService.getSession();
    }
}
