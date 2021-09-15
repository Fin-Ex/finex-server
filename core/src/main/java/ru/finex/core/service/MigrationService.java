package ru.finex.core.service;

/**
 * @author m0nster.mind
 */
public interface MigrationService extends Comparable<MigrationService> {

    int PRIORITY_CORE = 1;

    void migrateToLastVersion();
    void doneMigration();

    int getPriority();

    @Override
    default int compareTo(MigrationService o) {
        return Integer.compare(getPriority(), o.getPriority());
    }

}
