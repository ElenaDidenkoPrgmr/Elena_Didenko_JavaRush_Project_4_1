package ua.javarush.eldidenko.hibernate_todo_app.services;

import ua.javarush.eldidenko.hibernate_todo_app.dto.TaskDTO;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.mappers.TaskMapper;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.TaskRepository;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.UserRepository;
import ua.javarush.eldidenko.hibernate_todo_app.request.TaskRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = TaskMapper.INCTANCE;
    }

    @Override
    public TaskDTO fetchTasksById(Long taskId) {
        return taskMapper.taskToDTO(taskRepository.fetchTasksById(taskId));
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteTaskById(taskId);
    }

    @Override
    public List<TaskDTO> fetchTasksByUserId(Long id){
        return taskRepository.fetchTasksByUserId(id)
                .stream()
                .map(taskMapper::taskToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskRequest taskRequest, Long userId) {
        Task newTask = taskMapper.requestToTask(taskRequest);
        return taskMapper.taskToDTO(taskRepository.save(newTask, userId));
    }

    @Override
    public TaskDTO updateTask(TaskRequest taskRequest, Long taskId) {
        Task taskBefore = taskRepository.fetchTasksById(taskId);
        Task taskModified = taskMapper.updateTaskFromRequest(taskBefore, taskRequest);
        Task taskAfter = taskRepository.updateTask(taskModified);
        return taskMapper.taskToDTO(taskAfter);
    }
}
