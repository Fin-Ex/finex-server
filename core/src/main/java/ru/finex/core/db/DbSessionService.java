package ru.finex.core.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author m0nster.mind
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
