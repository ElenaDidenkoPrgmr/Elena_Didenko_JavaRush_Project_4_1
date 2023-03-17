package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;

public class UserRepositoryImpl implements UserRepository{
    private SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    /*@Override
    public Optional<User> fetchById(Long id) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }*/

    @Override
    public User fetchById(Long id) {
        try(Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }
    @Override
    public User fetchByUserName(String name) {
        try(Session session = sessionFactory.openSession()) {
            String hql = "from User u where u.username =: name";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("name", name);

            return query.uniqueResult();
        }
    }
    @Override
    public User save(User user) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user;
        }
    }
    @Override
    public User updateUser(User updateUser) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(updateUser);
            transaction.commit();
            return updateUser;
        }
    }

    @Override
    public void deleteUser(Long id) {
        try(Session session = sessionFactory.openSession()) {
            User deleteUser = fetchById(id);
            Transaction transaction = session.beginTransaction();
            session.remove(deleteUser);
            transaction.commit();
        }
    }
}
