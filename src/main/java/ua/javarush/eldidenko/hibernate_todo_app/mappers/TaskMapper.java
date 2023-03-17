package ua.javarush.eldidenko.hibernate_todo_app.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.javarush.eldidenko.hibernate_todo_app.dto.TaskDTO;
import ua.javarush.eldidenko.hibernate_todo_app.entites.Task;
import ua.javarush.eldidenko.hibernate_todo_app.request.TaskRequest;
@Mapper
public interface TaskMapper {
    TaskMapper INCTANCE = Mappers.getMapper(TaskMapper.class);

    Task requestToTask(TaskRequest taskRequest);

    TaskDTO taskToDTO(Task task);

}
