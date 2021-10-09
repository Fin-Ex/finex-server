package ru.finex.core.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author m0nster.mind
 */
public interface DbSessionService {

    SessionFactory getSessionFactory();
    Session openSession();
    Session getSession();

}
