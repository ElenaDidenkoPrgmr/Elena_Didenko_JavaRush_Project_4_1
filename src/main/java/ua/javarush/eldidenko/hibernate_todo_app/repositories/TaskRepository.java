package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> fetchTasksByUserId(Long userId);

    Task save(Task task, Long userId);

    Task updateTask(Task updateTask);

    Task fetchTasksById(Long taskId);

    void deleteTaskById(Long taskId);
}
