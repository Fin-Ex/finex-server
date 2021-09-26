package ru.finex.core.db.migration;

/**
 * @author m0nster.mind
 */
public interface MigrationService {

    /**
     * Автоматическая миграция зарегистрированных компонентов через {@link ru.finex.core.db.migration.Evolution}.
     */
    void autoMigration();

    /**
     * Миграция определенного компонента.
     * @param component компонент
     */
    void migrate(String component);

}
