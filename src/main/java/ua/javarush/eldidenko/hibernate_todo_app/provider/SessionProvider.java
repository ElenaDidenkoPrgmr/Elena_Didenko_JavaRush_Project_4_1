package ua.javarush.eldidenko.hibernate_todo_app.provider;

import org.hibernate.SessionFactory;

public interface SessionProvider {
    SessionFactory getSessionFactory();
}
