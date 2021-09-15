package ru.finex.core.db;

import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import ru.finex.core.EnvConfigurator;
import ru.finex.core.utils.Classes;

import java.net.URL;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author m0nster.mind
 */
@Singleton
public class FlywayFactory {

    private final URL hibernateConfig;
    private final EnvConfigurator configurator;

    @Inject
    public FlywayFactory(@Named("HibernateConfig") URL hibernateConfig, EnvConfigurator configurator) {
        this.hibernateConfig = hibernateConfig;
        this.configurator = configurator;
    }

    public Flyway create(String service) {
        Configuration configuration = new Configuration().configure(hibernateConfig);
        Properties properties = configuration.getProperties();
        configurator.configure(properties);
        Class<?> providerClass = Classes.getClass(properties.getProperty("hibernate.connection.provider_class"));
        ConnectionProvider connectionProvider = (ConnectionProvider) Classes.createInstance(providerClass);
        if (connectionProvider instanceof Configurable) {
            ((Configurable) connectionProvider).configure(properties);
        }

        DataSource dataSource = connectionProvider.unwrap(DataSource.class);

        return Flyway.configure()
            .locations("evolution")
            .dataSource(dataSource)
            .sqlMigrationPrefix(service + "_v")
            .undoSqlMigrationPrefix(service + "_u")
            .repeatableSqlMigrationPrefix(service + "_r")
            .table(service + "_evolutions")
            .load();
    }



}
