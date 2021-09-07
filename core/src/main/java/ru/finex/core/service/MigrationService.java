package ru.finex.core.service;

/**
 * @author m0nster.mind
 */
public interface MigrationService {

    void migrateToLastVersion();
    void doneMigration();

}
