package ru.finex.core.db.migration.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author m0nster.mind
 */
@Singleton
@SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:Indentation"})
public class MigrationDao {

    private final Gson gson = new Gson();
    private final DataSource dataSource;

    @Inject
    public MigrationDao(@Named("Migration") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void install() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint();

            try {
                Statement statement = connection.createStatement();
                statement.addBatch(MigrationConsts.MIGRATION_TABLE);
                statement.addBatch(MigrationConsts.MIGRATION_INDEX);
                statement.executeBatch();
                statement.close();
            } catch (SQLException e) {
                connection.rollback(savepoint);
                connection.commit();
                connection.setAutoCommit(true);
                throw e;
            }

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getChecksumsByComponent(String component) {
        String query = """
            select checksum
            from db_evolutions
            where component = ?
            order by version asc
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, component);
            try (ResultSet results = statement.executeQuery()) {
                List<String> result = new ArrayList<>();
                while (results.next()) {
                    result.add(results.getString(1));
                }

                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("checkstyle:NestedTryDepth")
    public void rollbackAndDeleteRecursive(String component, int version) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint();

            try {
                List<String> queries = getDownQueriesByComponentAndUpperVersion(connection, component, version);
                try (Statement statement = connection.createStatement()) {
                    for (String query : queries) {
                        statement.addBatch(query);
                    }

                    statement.executeBatch();
                }

                delete(connection, component, version);
            } catch (SQLException e) {
                connection.rollback(savepoint);
                connection.commit();
                connection.setAutoCommit(true);
                throw e;
            }

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private List<String> getDownQueriesByComponentAndUpperVersion(Connection connection, String component, int version) throws SQLException {
        String query = """
            select down_queries
            from db_evolutions
            where component = ? and version >= ?
            order by version desc
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, component);
            statement.setInt(2, version);
            try (ResultSet results = statement.executeQuery()) {
                if (!results.first()) {
                    throw new NullPointerException(String.format(
                        "Down queries not found for %s component and %d version.",
                        component, version
                    ));
                }
                List<String> result = new ArrayList<>();
                while (results.next()) {
                    List<String> queries = gson.fromJson(
                        results.getString(1),
                        new TypeToken<List<String>>() { }.getType()
                    );
                    result.addAll(queries);
                }

                return result;
            }
        }
    }

    private void delete(Connection connection, String component, int version) throws SQLException {
        String query = """
            delete from db_evolutions
            where component = ? and version >= ?
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, component);
            statement.setInt(2, version);
            statement.execute();
        }
    }

    public void applyAndSave(MigrationData data, String checksum) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint();

            try {
                apply(connection, data.getUpQueries());
                save(connection, data, checksum);
            } catch (SQLException e) {
                connection.rollback(savepoint);
                connection.commit();
                connection.setAutoCommit(false);
                throw e;
            }

            connection.commit();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void apply(Connection connection, List<String> queries) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            for (String query : queries) {
                statement.addBatch(query);
            }

            statement.executeBatch();
        }
    }

    private void save(Connection connection, MigrationData data, String checksum) throws SQLException {
        String query = """
            insert into db_evolutions(
                component,
                version,
                checksum,
                up_queries,
                down_queries
            ) values (?, ?, ?, ?::json, ?::json)
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, data.getComponent());
            statement.setInt(2, data.getVersion());
            statement.setString(3, checksum);
            statement.setObject(4, gson.toJson(data.getUpQueries()));
            statement.setObject(5, gson.toJson(data.getDownQueries()));
            statement.execute();
        }
    }

}
