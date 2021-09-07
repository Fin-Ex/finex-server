package ru.finex.core.service;

import org.hibernate.Session;

/**
 * @author m0nster.mind
 */
public interface DbSessionService {

    Session openSession();
    Session getSession();

}
