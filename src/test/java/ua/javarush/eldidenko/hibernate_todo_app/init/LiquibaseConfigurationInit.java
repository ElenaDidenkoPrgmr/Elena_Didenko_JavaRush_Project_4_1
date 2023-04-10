package ua.javarush.eldidenko.hibernate_todo_app.init;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.UserRepositoryImplTest;

import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.String.format;

public class LiquibaseConfigurationInit {
    PostgreSQLContainer postgreSQLContainer;

    public LiquibaseConfigurationInit(PostgreSQLContainer postgreSQLContainer) {
        this.postgreSQLContainer = postgreSQLContainer;
    }

    public void update() throws SQLException, LiquibaseException {
        HikariConfig dsConfig = new HikariConfig();
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(UserRepositoryImplTest.class.getClassLoader());

        dsConfig.setJdbcUrl(format("jdbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getContainerIpAddress(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgreSQLContainer.getDatabaseName()));

        dsConfig.setUsername(postgreSQLContainer.getUsername());
        dsConfig.setPassword(postgreSQLContainer.getPassword());
        dsConfig.setDriverClassName("org.postgresql.Driver");
        dsConfig.setMaximumPoolSize(5);
        dsConfig.setMinimumIdle(2);

        HikariDataSource ds = new HikariDataSource(dsConfig);
        Connection connection = ds.getConnection();

        JdbcConnection jdbcConnection = new JdbcConnection(connection);
        Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);

        Liquibase liquiBase = new Liquibase("/liquibase/db.changelog-root.xml", resourceAccessor, db);
        liquiBase.update();
    }
}
