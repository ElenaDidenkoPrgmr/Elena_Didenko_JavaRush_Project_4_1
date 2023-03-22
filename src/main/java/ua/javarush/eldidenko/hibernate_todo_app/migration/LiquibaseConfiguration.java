package ua.javarush.eldidenko.hibernate_todo_app.migration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.StartupFailedException;
import ua.javarush.eldidenko.hibernate_todo_app.utils.DbSettings;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class LiquibaseConfiguration {
    private static final Logger LOGGER = LogManager.getLogger(LiquibaseConfiguration.class);
    private static final String CHANGELOG_FILE = "/liquibase/db.changelog-root.xml";
    public static final String DEFAULT_CONNECTION_POOL_SIZE = "5";

    public void update() {
        HikariConfig config = new HikariConfig(readEnvProperties());
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());

        try (HikariDataSource ds = new HikariDataSource(config);
             Connection connection = ds.getConnection()) {

            JdbcConnection jdbcConnection = new JdbcConnection(connection);
            Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);

            Liquibase liquiBase = new Liquibase(CHANGELOG_FILE, resourceAccessor, db);

            liquiBase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new StartupFailedException("Can;t startup. Reason: " + e.getMessage(), e);
        }
    }

    private Properties readEnvProperties() {
        DbSettings dbSettings = new DbSettings();

        Properties properties = new Properties();
        properties.put("driverClassName", dbSettings.dbProperties().getProperty("driver_class"));
        properties.put("jdbcUrl", dbSettings.dbProperties().getProperty("url"));
        properties.put("schema", dbSettings.dbProperties().getProperty("liquibase.schema"));
        properties.put("username", dbSettings.dbProperties().getProperty("username"));
        properties.put("password", dbSettings.dbProperties().getProperty("password"));

        String connectionPoolSize = System.getenv("LIQUIBASE_CONNECTION_POOL_SIZE");
        if (connectionPoolSize != null) {
            properties.put("maximumPoolSize", connectionPoolSize);
        } else {
            properties.put("maximumPoolSize", DEFAULT_CONNECTION_POOL_SIZE);
        }

        return properties;
    }
}
