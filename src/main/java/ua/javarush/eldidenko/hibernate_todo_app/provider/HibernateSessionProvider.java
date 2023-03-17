package ua.javarush.eldidenko.hibernate_todo_app.provider;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.entites.UserToken;

public class HibernateSessionProvider implements SessionProvider{
    @Override
    public SessionFactory getSessionFactory() {
        return new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserToken.class)
                .addAnnotatedClass(Task.class)
                .buildSessionFactory();
    }
}
