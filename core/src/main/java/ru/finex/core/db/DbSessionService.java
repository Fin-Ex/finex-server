package ru.finex.core.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Сервис для работы с сессией БД.
 *
 * @author m0nster.mind
 * @see Session
 */
public interface DbSessionService {

    /**
     * Session factory.
     * @return session factory
     * @see SessionFactory
     */
    SessionFactory getSessionFactory();

    /**
     * Opens a new database session.
     * @return session
     */
    Session openSession();

    /**
     * Try to get a current database session.
     * @return session
     */
    Session getSession();

}
