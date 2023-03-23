package ua.javarush.eldidenko.hibernate_todo_app.provider;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.entites.UserToken;
import ua.javarush.eldidenko.hibernate_todo_app.utils.DbSettings;

import java.util.Properties;

public class HibernateSessionProvider implements SessionProvider{
    @Override
    public SessionFactory getSessionFactory() {
        DbSettings dbSettings = new DbSettings();

        Properties properties = new Properties();
        properties.put(Environment.DRIVER, dbSettings.dbProperties().getProperty("driver_class"));
        properties.put(Environment.URL, dbSettings.dbProperties().getProperty("url"));
        properties.put(Environment.USER, System.getenv("DB_USER"));
        properties.put(Environment.PASS, System.getenv("DB_PASS"));
        properties.put(Environment.DIALECT, dbSettings.dbProperties().getProperty("dialect"));
        properties.put(Environment.SHOW_SQL, dbSettings.dbProperties().getProperty("hibernate.show_sql"));
        properties.put(Environment.HBM2DDL_AUTO, dbSettings.dbProperties().getProperty("hibernate.hbm2ddl.auto"));

        return new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserToken.class)
                .addAnnotatedClass(Task.class)
                .setProperties(properties)
                .buildSessionFactory();
    }
}
