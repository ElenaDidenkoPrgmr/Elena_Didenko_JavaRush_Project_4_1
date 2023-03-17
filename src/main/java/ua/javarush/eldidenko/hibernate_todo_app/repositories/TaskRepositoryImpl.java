package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepository{
    private SessionFactory sessionFactory;

    public TaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Task> fetchTasksByUserId(Long userId){
        try (Session session = sessionFactory.openSession()) {
            String hql = "from Task where user.id = :userId";
            Query<Task> query = session.createQuery(hql, Task.class);
            query.setParameter("userId",userId);
            return query.list();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
            //return List.of();
        }
    }

    @Override
    public Task save(Task task) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
            return task;
        }
    }
}
