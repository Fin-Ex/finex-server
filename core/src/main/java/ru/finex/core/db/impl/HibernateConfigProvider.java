package ru.finex.core.db.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class HibernateConfigProvider implements Provider<URL> {

    @Override
    public URL get() {
        File file = new File("resources/hibernate.xml");
        if (file.exists()) {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return getClass().getClassLoader().getResource("hibernate.xml");
    }

}
