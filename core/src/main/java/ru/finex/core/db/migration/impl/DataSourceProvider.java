package ru.finex.core.db.migration.impl;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import ru.finex.core.EnvConfigurator;
import ru.finex.core.utils.ClassUtils;

import java.net.URL;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * @author m0nster.mind
 */
public class DataSourceProvider implements Provider<DataSource> {

    private final URL hibernateConfig;
    private final EnvConfigurator configurator;

    @Inject
    public DataSourceProvider(@Named("HibernateConfig") URL hibernateConfig, EnvConfigurator configurator) {
        this.hibernateConfig = hibernateConfig;
        this.configurator = configurator;
    }

    @Override
    public DataSource get() {
        Configuration configuration = new Configuration().configure(hibernateConfig);
        Properties properties = configuration.getProperties();
        configurator.configure(properties);
        Class<?> providerClass = ClassUtils.forName(properties.getProperty("hibernate.connection.provider_class"));
        ConnectionProvider connectionProvider = (ConnectionProvider) ClassUtils.createInstance(providerClass);
        if (connectionProvider instanceof Configurable configurable) {
            configurable.configure(properties);
        }

        return connectionProvider.unwrap(DataSource.class);
    }
}
