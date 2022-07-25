package ru.finex.core.db.migration.impl;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import ru.finex.core.utils.ClassUtils;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * @author m0nster.mind
 */
public class DataSourceProvider implements Provider<DataSource> {

    private final Map<String, Object> hibernateProperties;

    @Inject
    public DataSourceProvider(@Named("HibernateProperties") Map<String, Object> hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    @Override
    public DataSource get() {
        String providerPath = (String) hibernateProperties.get("hibernate.connection.provider_class");
        Class<?> providerClass = ClassUtils.forName(providerPath);
        ConnectionProvider connectionProvider = (ConnectionProvider) ClassUtils.createInstance(providerClass);
        if (connectionProvider instanceof Configurable configurable) {
            configurable.configure(hibernateProperties);
        }

        return connectionProvider.unwrap(DataSource.class);
    }
}
