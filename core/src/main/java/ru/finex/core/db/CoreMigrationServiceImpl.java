package ru.finex.core.db;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import ru.finex.core.service.MigrationService;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class CoreMigrationServiceImpl implements MigrationService {

    private final Flyway flyway;

    @Inject
    public CoreMigrationServiceImpl(FlywayFactory flywayFactory) {
        flyway = flywayFactory.create("core");
    }

    @Override
    public void migrateToLastVersion() {
        log.debug("Begin core migration.");
        MigrateResult migrate = flyway.migrate();
        log.info("Executed migrations: {}, migrated from {} to {} version.",
            migrate.migrationsExecuted, migrate.initialSchemaVersion, migrate.targetSchemaVersion);
        if (!migrate.warnings.isEmpty()) {
            migrate.warnings.forEach(log::warn);
        }
        log.debug("End core migration.");
    }

    @Override
    public void doneMigration() {
        DataSource dataSource = flyway.getConfiguration().getDataSource();
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public int getPriority() {
        return PRIORITY_CORE;
    }

}
