package ua.javarush.eldidenko.hibernate_todo_app.init;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.entites.UserToken;
import ua.javarush.eldidenko.hibernate_todo_app.provider.SessionProvider;

import java.util.Properties;

import static java.lang.String.format;

public class HibernateSessionProviderInit implements SessionProvider {
    private final PostgreSQLContainer postgreSQLContainer;

    public HibernateSessionProviderInit(PostgreSQLContainer postgreSQLContainer) {
        this.postgreSQLContainer = postgreSQLContainer;
    }

    @Override
    public SessionFactory getSessionFactory() {
        // DbSettings dbSettings = new DbSettings();

        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.postgresql.Driver");
        properties.put(Environment.URL, format("jdbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getContainerIpAddress(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgreSQLContainer.getDatabaseName()));
        properties.put(Environment.USER, "sa");
        properties.put(Environment.PASS, "sa");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        //properties.put(Environment.SHOW_SQL, dbSettings.dbProperties().getProperty("hibernate.show_sql"));
        //properties.put(Environment.HBM2DDL_AUTO, dbSettings.dbProperties().getProperty("hibernate.hbm2ddl.auto"));

        return new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserToken.class)
                .addAnnotatedClass(Task.class)
                .setProperties(properties)
                .buildSessionFactory();
    }
}
