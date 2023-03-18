package ua.javarush.eldidenko.hibernate_todo_app.services;

import ua.javarush.eldidenko.hibernate_todo_app.dto.TaskDTO;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.mappers.TaskMapper;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.TaskRepository;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.UserRepository;
import ua.javarush.eldidenko.hibernate_todo_app.request.TaskRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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
        User user = userRepository.fetchById(userId);
        Task newTask = taskMapper.requestToTask(taskRequest);
        newTask.setUser(user);

        return taskMapper.taskToDTO(taskRepository.save(newTask));
    }

    @Override
    public TaskDTO updateTask(TaskRequest taskRequest, Long taskId) {
        Task taskBefore = taskRepository.fetchTasksById(taskId);
        Task taskModified = taskMapper.updateTaskFromRequest(taskBefore, taskRequest);
        Task taskAfter = taskRepository.updateTask(taskModified);
        return taskMapper.taskToDTO(taskAfter);
    }
}
