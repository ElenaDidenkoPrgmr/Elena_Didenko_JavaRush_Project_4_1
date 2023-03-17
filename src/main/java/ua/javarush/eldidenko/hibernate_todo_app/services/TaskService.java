package ua.javarush.eldidenko.hibernate_todo_app.services;

import ua.javarush.eldidenko.hibernate_todo_app.dto.TaskDTO;
import ua.javarush.eldidenko.hibernate_todo_app.request.TaskRequest;

import java.util.List;

public interface TaskService {
    List<TaskDTO> fetchTasksByUserId(Long id);

    TaskDTO createTask(TaskRequest taskRequest, Long UserId);
}
