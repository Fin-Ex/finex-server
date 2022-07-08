package ru.finex.core.db.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Convert all database entities (fields, tables and other) to snake case from logical view.
 *
 * @author m0nster.mind
 * @see SnakeCaseStrategy
 */
public class SnakeCasePhysicalNamingStrategy implements PhysicalNamingStrategy {

    private final SnakeCaseStrategy caseStrategy = new SnakeCaseStrategy();

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return Identifier.toIdentifier(caseStrategy.translate(name.getText()));
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return Identifier.toIdentifier(caseStrategy.translate(name.getText()));
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return Identifier.toIdentifier(caseStrategy.translate(name.getText()));
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return Identifier.toIdentifier(caseStrategy.translate(name.getText()));
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return Identifier.toIdentifier(caseStrategy.translate(name.getText()));
    }

}
