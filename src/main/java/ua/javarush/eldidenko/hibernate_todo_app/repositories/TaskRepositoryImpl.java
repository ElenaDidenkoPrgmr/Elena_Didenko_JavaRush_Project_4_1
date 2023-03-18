package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepository{
    private SessionFactory sessionFactory;

    public TaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task fetchTasksById(Long taskId) {
        try(Session session = sessionFactory.openSession()) {
            return session.get(Task.class, taskId);
        }
    }

    @Override
    public void deleteTaskById(Long taskId) {
        try(Session session = sessionFactory.openSession()) {
            Task deleteTask = fetchTasksById(taskId);
            Transaction transaction = session.beginTransaction();
            session.remove(deleteTask);
            transaction.commit();
        }
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

    @Override
    public Task updateTask(Task updateTask) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(updateTask);
            transaction.commit();
            return updateTask;
        }
    }
}
